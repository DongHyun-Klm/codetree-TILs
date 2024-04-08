import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static StringBuilder sb = new StringBuilder();
	static int q, n, m;
	static Belt[] belts;
	static boolean[] broken;
	static int[][] boxs;
	static class Box {
		int id, w;
		Box pre, next;
		
		public Box(int id, int w) {
			this.id = id;
			this.w = w;
		}
	}
	
	static class Belt {
		Box first, last;
		Set<Integer> ids = new HashSet<Integer>();
		
		public Belt(Box first) {
			this.first = first;
			this.last = first;
			ids.add(first.id);
		}
	}
	
    public static void main(String[] args) throws IOException {
    	input();
    	// 공장 설립
    	belts = new Belt[m+1];
    	broken = new boolean[m+1];
    	make();
    	for (int i = 1; i < q; i++) {
    		st = new StringTokenizer(br.readLine());
    		int command = Integer.parseInt(st.nextToken());
    		switch (command) {
    		// 물건 하차
			case 200:
				int w_max = Integer.parseInt(st.nextToken());
				down(w_max);
				break;
				
			case 300:
				int r_id = Integer.parseInt(st.nextToken());
				remove(r_id);
				break;
				
			case 400:
				int f_id = Integer.parseInt(st.nextToken());
				check(f_id);
				break;
				
			case 500:
				int b_num = Integer.parseInt(st.nextToken());
				brk(b_num);
				break;

			default:
				break;
			}
		}
    	System.out.print(sb);
    }

	private static void brk(int b_num) {
		if(broken[b_num]) {
			sb.append(-1).append('\n');
			return;
		}
		broken[b_num] = true;
		for (int j = 1; j < m; j++) {
			int index = (b_num + j - 1) % m + 1;
			if(broken[index]) continue;
			Box last = belts[index].last;
			last.next = belts[b_num].first;
			belts[b_num].first.pre = last;
			last.next.pre = last;
			belts[index].last = belts[b_num].last;
			belts[b_num].first = null;
			belts[b_num].last = null;
			for(int t : belts[b_num].ids) {
				belts[index].ids.add(t);
			}
			belts[b_num].ids.clear();
			break;
		}
		sb.append(b_num).append('\n');
	}


	private static void check(int f_id) {
		boolean exist = false;
		for (int i = 1; i <= m; i++) {
			if(exist || !belts[i].ids.contains(f_id)) continue;
			Box now = belts[i].first;
			sb.append(i).append('\n');
			while(true) {
				if(now.id == f_id) {
					// 확인하는 상자가 맨 앞인 경우
					if(now.pre == null) {
						
					} // 확인하는 상자가 맨 뒤인 경우
					else if(now.next == null) {
						belts[i].last = now.pre;
						belts[i].last.next = null;
						belts[i].first.pre = now;
						now.next = belts[i].first;
						now.pre = null;
						belts[i].first = now;
					} // 중간인 경우
					else {
						Box first = belts[i].first;
						Box last = belts[i].last;
						Box pre = now.pre;
						now.pre = null;
						belts[i].first = now;
						last.next = first;
						first.pre = last;
						belts[i].last = pre;
						belts[i].last.next = null;
					}
					exist = true;
				}
				if(exist) break;
				now = now.next;
			}
			
		}
		
		
		
		if(!exist) sb.append(-1).append('\n');
	}


	private static void remove(int r_id) {
		boolean exist = false;
		for (int i = 1; i <= m; i++) {
			if(exist || !belts[i].ids.contains(r_id)) continue;
			Box now = belts[i].first;
			while(true) {
				if(now.id == r_id) {
					belts[i].ids.remove(r_id);
					// 제거하는 상자가 맨 앞인 경우
					if(now.pre == null) {
						belts[i].first = now.next;
						belts[i].first.pre = null;
					} // 제거하는 상자가 맨 뒤인 경우
					else if(now.next == null) {
						belts[i].last = now.pre;
						belts[i].last.next = null;
					} // 중간인 경우
					else {
						Box back = now.next;
						now.pre.next = back;
						back.pre = now.pre;
					}
					exist = true;
				}
				if(exist) break;
				now = now.next;
			}
		}
		if(exist) sb.append(r_id).append('\n');
		else sb.append(-1).append('\n');
	}


	private static void down(int w_max) {
		long sum = 0;
		for (int i = 1; i <= m; i++) {
			if(belts[i].ids.isEmpty()) continue;
			Box first = belts[i].first;
			// w_max 이하라면 하차
			if(first.w <= w_max) {
				sum += first.w;
				belts[i].ids.remove(first.id);
				belts[i].first = first.next;
			}
			// 아니라면 맨 뒤로
			else {
				belts[i].last.next = first;
				first.pre = belts[i].last;
				belts[i].first = first.next;
				belts[i].first.pre = null;
				first.next = null;
				belts[i].last = first;
			}
			belts[i].first.pre = null;
		}
		sb.append(sum).append('\n');
	}


	private static void make() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < n; j++) {
				boxs[j][i] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 1; i <= m; i++) {
			Box pre = null;
			for (int j = 0; j < n/m; j++) {
				Box box = new Box(boxs[(n/m) * (i-1) + j][0], boxs[(n/m) * (i-1) + j][1]);
				if(j==0) {
					belts[i] = new Belt(box);
				}
				else {
					pre.next = box;
					box.pre = pre;
					belts[i].ids.add(box.id);
					if(j == n/m - 1) belts[i].last = box;
				}
				pre = box;
			}
		}
		
	}


	private static void input() throws IOException {
		q = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		int start = Integer.parseInt(st.nextToken());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		boxs = new int[n][2];
	}
}