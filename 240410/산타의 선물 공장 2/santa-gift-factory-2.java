import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static StringBuilder sb = new StringBuilder();
	static int q, n, m;
	static Belt[] belts;
	static Map<Integer, Box> boxMap = new HashMap<Integer, Main.Box>();
	
	static class Box {
		int num;
		Box head, tail;
		public Box(int num) {
			this.num = num;
		}
	}
	
	static class Belt {
		Box first, last;
		int size;
		public Belt(Box first) {
			this.first = first;
			this.last = first;
			this.size = 1;
		}
		public Belt() {
			this.size = 0;
		}
		// 위에 박스 추가
		public void push(Box box) {
			size++;
			box.head = last;
			last.tail = box;
			last = box;
		}
		
		public void pushFirst(Box box) {
			size++;
			if(first == null) {
				this.first = box;
				this.last = box;
				return;
			}
			first.head = box;
			box.tail = first;
			first = box;
		}
		
		public Box pollFirst() {
			Box box = first;
			if(box == null) return null;
			first = box.tail;
			if(first != null) first.head = null;
			else last = null;
			size--;
			box.tail = null;
			return box;
		}
		
	}
	
    public static void main(String[] args) throws NumberFormatException, IOException {
    	input();
    	for (int i = 1; i < q; i++) {
    		st = new StringTokenizer(br.readLine());
    		int command = Integer.parseInt(st.nextToken());
    		switch (command) {
			case 200:
				int m_src = Integer.parseInt(st.nextToken());
				int m_dst = Integer.parseInt(st.nextToken());
				move(m_src, m_dst);
				break;
				
			case 300:
				m_src = Integer.parseInt(st.nextToken());
				m_dst = Integer.parseInt(st.nextToken());
				trade(m_src, m_dst);
				break;
				
			case 400:
				m_src = Integer.parseInt(st.nextToken());
				m_dst = Integer.parseInt(st.nextToken());
				divid(m_src, m_dst);
				break;
				
			case 500:
				int p_num = Integer.parseInt(st.nextToken());
				getBox(p_num);
				break;
				
			case 600:
				int b_num = Integer.parseInt(st.nextToken());
				getBelt(b_num);
				break;

			default:
				break;
			}
		}
    	System.out.print(sb);
    }

	private static void getBelt(int b_num) {
		Belt belt = belts[b_num];
		int a = -1, b = -1, c = belt.size;
		if(belt.first != null) a = belt.first.num;
		if(belt.last != null) b = belt.last.num;
		sb.append(a + 2 * b + 3 * c).append('\n');
	}

	private static void getBox(int p_num) {
		Box box = boxMap.get(p_num);
		int a = -1, b = -1;
		if(box.head != null) a = box.head.num;
		if(box.tail != null) b = box.tail.num;
		
		sb.append(a + 2 * b).append('\n');
	}

	private static void divid(int m_src, int m_dst) {
		Belt from = belts[m_src];
		Belt to = belts[m_dst];
		int size = from.size;
		if(size <= 1) {
			sb.append(to.size).append('\n');
			return;
		}
		
		from.size -= size/2;
		to.size += size/2;
		Box first = from.first;
		Box last = from.first;
		for (int i = 1; i < size/2; i++) {
			last = last.tail;
		}
		from.first = last.tail;
		from.first.head = null;
		last.tail = to.first;
		if(to.first != null) to.first.head = last;
		else to.last = last;
		to.first = first;
		
		sb.append(to.size).append('\n');
	}

	private static void trade(int m_src, int m_dst) {
		Belt from = belts[m_src];
		Belt to = belts[m_dst];
		if(from.first == null && to.first == null) {
			sb.append(to.size).append('\n');
			return;
		}
		
		Box from_first = from.pollFirst();
		Box to_first = to.pollFirst();
		if(from_first != null) to.pushFirst(from_first);
		if(to_first != null) from.pushFirst(to_first);
		
		sb.append(to.size).append('\n');
	}

	private static void move(int m_src, int m_dst) {
		Belt from = belts[m_src];
		Belt to = belts[m_dst];
		if(from.first == null) {
			sb.append(to.size).append('\n');
			return;
		}
		Box from_last = from.last;
		Box to_first = to.first;
		to.size += from.size;
		from.size = 0;
		if(to_first == null) {
			to.last = from.last;
		}
		else {
			to_first.head = from_last;
			from_last.tail = to.first;
		}
		to.first = from.first;
		from.first = null;
		from.last = null;
		
		
		sb.append(to.size).append('\n');
	}

	private static void input() throws IOException {
		q = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		st.nextToken(); // 100
		n = Integer.parseInt(st.nextToken());
		belts = new Belt[n+1];
		for (int i = 1; i <= n; i++) {
			belts[i] = new Belt();
		}
		m = Integer.parseInt(st.nextToken());
		for (int i = 1; i <= m; i++) {
			int index = Integer.parseInt(st.nextToken());
			Box box = new Box(i);
			boxMap.put(i, box);
			if(belts[index].first == null) {
				
				belts[index] = new Belt(box);
			} else {
				belts[index].push(box);
			}
		}
	}
}