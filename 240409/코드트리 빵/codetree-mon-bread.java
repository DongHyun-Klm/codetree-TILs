import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static int n, m;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static ArrayList<int[]> bases = new ArrayList<int[]>();
	static person[] people;
	static boolean[][] road;
	static boolean[] arrive;
	static int[][] dir = {{1,0},{0,1},{0,-1},{-1,0}};
	
	static class person {
		int r, c, target_r, target_c;

		public person(int r, int c, int target_r, int target_c) {
			this.r = r;
			this.c = c;
			this.target_r = target_r;
			this.target_c = target_c;
		}

		public void goBase() {
			PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a,b) -> {
				if(a[0] == b[0]) {
					if(a[1] == b[1]) {
						return a[2] - b[2];
					} else return a[1] - b[1];
				} else return a[0] - b[0];
			});
			for(int[] b : bases) {
				int dis = 0;
				Queue<int[]> q = new LinkedList<int[]>();
				boolean[][] visit = new boolean[n][n];
				q.add(new int[] {b[0], b[1], 0});
				visit[b[0]][b[1]] = true;
				o:
				while(!q.isEmpty()) {
					int[] now = q.poll();
					for (int k = 0; k < 4; k++) {
						int nr = now[0] + dir[k][0];
						int nc = now[1] + dir[k][1];
						if(nr<0||nc<0||nr>=n||nc>=n) continue;
						if(road[nr][nc]) continue;
						if(visit[nr][nc]) continue;
						q.add(new int[] {nr, nc, now[2] + 1});
						visit[nr][nc] = true;
						if(nr == target_r && nc == target_c) {
							dis = now[2] + 1;
							break o;
						}
					}
				}
				
				if(dis != 0) pq.add(new int[] {dis, b[0], b[1]});
			}
			int[] target_base = pq.poll();
			this.r = target_base[1];
			this.c = target_base[2];
			for(int i=0; i<bases.size(); i++) {
				int[] now = bases.get(i);
				if(now[0] == this.r && now[1] == this.c) {
					bases.remove(i);
					road[now[0]][now[1]] = true;
					break;
				}
			}
		}

		public void move() {
			Queue<int[]> q = new LinkedList<int[]>();
			q.add(new int[] {target_r,target_c});
			int[][] trace = new int[n][n];
			trace[target_r][target_c] = 5;
			o:
			while(!q.isEmpty()) {
				int[] now = q.poll();
				for (int k = 0; k < 4; k++) {
					int nr = now[0] + dir[k][0];
					int nc = now[1] + dir[k][1];
					if(nr<0||nc<0||nr>=n||nc>=n) continue;
					if(nr == r && nc == c) {
						trace[nr][nc] = k+1;
						break o;
					}
					if(road[nr][nc]) continue;
					if(trace[nr][nc] > 0) continue;
					q.add(new int[] {nr, nc});
					trace[nr][nc] = k+1;	
				}
			}
			int k = trace[r][c] - 1;
			if(k==0) k = 3;
			else if(k==1) k = 2;
			else if(k==2) k = 1;
			else k = 0;
			this.r = r + dir[k][0];
			this.c = c + dir[k][1];
		}
	}
	
    public static void main(String[] args) throws IOException {
    	input();
    	int t = 0;
    	while(true) {
    		t++;
    		int move_num = Math.min(t-1, m);
    		for (int i = 1; i <= move_num; i++) {
    			if(arrive[i]) continue;
    			person now = people[i];
				// 1. 1칸 움직이기
    			now.move();
    			
    			// 2. 도착했다면 표시해놓기
    			if(now.r == now.target_r && now.c == now.target_c) {
    				arrive[i] = true;
    			}
    			
			}
    		
    		// 2. 도착했다면 멈춤
    		for (int i = 1; i <= m; i++) {
				if(arrive[i]) {
					road[people[i].r][people[i].c] = true;
				}
			}
    		
    		// 3. t 사람은 베이스캠프 들어가기
    		if(t<=m) {
    			person now = people[t];
    			now.goBase();
    		}
    		boolean flag = true;
    		for (int i = 1; i <= m; i++) {
				if(!arrive[i]) {
					flag = false;
					break;
				}
			}
    		if(flag) break;
    	}
    	System.out.println(t);
    }

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		people = new person[m+1];
		arrive = new boolean[m+1];
		road = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				int now = Integer.parseInt(st.nextToken());
				if(now==1) bases.add(new int[] {i,j});
			}
		}
		
		for (int i = 1; i <= m; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			people[i] = new person(-1, -1, r, c);
		}
		
	}
}