import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int m, t, pack_r, pack_c;
	static int[][] arr = new int[4][4];
	static int[][] dumy = new int[4][4];
	static int[][] dir = {{-1,0},{-1,-1},{0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1}};
	static int[][] dir2 = {{-1,0},{0,-1},{1,0},{0,1}};
	static int[][] mon;
	static boolean[][] egg;
	static ArrayList<monster> al = new ArrayList<>();
	static class info {
		String dir;
		int eat;
		
		info(String dir, int eat) {
			this.dir = dir;
			this.eat = eat;
		}
		
	}
	static class monster {
		int r, c, d;

		monster(int r, int c, int d) {
			this.r = r;
			this.c = c;
			this.d = d;
		}

		public void move() {
			int temp_d = this.d;
			for (int i = 0; i < 8; i++) {
				int nr = this.r + dir[temp_d][0];
				int nc = this.c + dir[temp_d][1];
				if(nr<0||nc<0||nr>=4||nc>=4||(nr==pack_r && nc==pack_c)
						||(dumy[nr][nc]>0)) {
					temp_d++;
					temp_d%=8;
					continue;
				}
				this.r = nr;
				this.c = nc;
				this.d = temp_d;
				break;
			}
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		input();
		
		while(t-->0) {
			// 1. 몬스터 복제 시도
			ArrayList<monster> copy = new ArrayList<>();
			for(monster m : al) {
				copy.add(new monster(m.r, m.c, m.d));
			}
			
			// 2. 몬스터 이동
			mon = new int[4][4];
			for(monster m : al) {
				m.move();
				mon[m.r][m.c]++;
			}
			
			// 3. 팩맨 이동
			pack_move();
			
			// 4. 몬스터 시체 소멸
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if(dumy[i][j] > 0) dumy[i][j]--;
				}
			}
			
			// 5. 몬스터 복제 완성
			for(monster m : copy) {
				al.add(m);
			}
			
		}
		System.out.print(al.size());
		
	}

	private static void pack_move() {
		PriorityQueue<info> pq = new PriorityQueue<>((a,b) -> {
			if(a.eat == b.eat) return Integer.parseInt(a.dir) - Integer.parseInt(b.dir);
			return b.eat - a.eat;
		});
		
		for (int i = 0; i < 4; i++) {
			int[][] copy = new int[4][4];
			for (int j = 0; j < 4; j++) {
				copy[j] = mon[j].clone();
			}
			rec(pack_r, pack_c, i, 0, "", pq, copy);
		}
		
		char[] route = pq.poll().dir.toCharArray();
		for(char c : route) {
			int d = c - '0';
			pack_r += dir2[d][0];
			pack_c += dir2[d][1];
			
			for (int i = 0; i < al.size(); i++) {
				monster now = al.get(i);
				if(now.r == pack_r && now.c == pack_c) {
					al.remove(i);
					dumy[now.r][now.c] = 2;
					i--;
				}
			}
		}
	}

	private static void rec(int r, int c, int d, int eat, String dir, PriorityQueue<info> pq, int[][] m) {
		if(dir.length()==3) {
			pq.add(new info(dir, eat));
			return;
		}
		
		int nr = r + dir2[d][0];
		int nc = c + dir2[d][1];
		if(nr<0||nc<0||nr>=4||nc>=4) return;
		if(mon[nr][nc] > 0) {
			for (int i = 0; i < 4; i++) {
				int[][] copy = new int[4][4];
				for (int j = 0; j < 4; j++) {
					copy[j] = m[j].clone();
				}
				copy[nr][nc] = 0;
				rec(nr, nc, i, eat + m[nr][nc], dir + d, pq, copy);
			}
		}
		else {
			for (int i = 0; i < 4; i++) {
				rec(nr, nc, i, eat, dir + d, pq, m);
			}
		}
	}

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		m = Integer.parseInt(st.nextToken());
		t = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());		
		pack_r = Integer.parseInt(st.nextToken()) - 1;
		pack_c = Integer.parseInt(st.nextToken()) - 1;
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());		
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken()) - 1;
			al.add(new monster(r, c, d));
		}
	}
}