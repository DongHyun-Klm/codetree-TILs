import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int N, M, K, out_r, out_c, answer = 0;
	static int[][] arr;
	static int[][] dir = {{1,0},{-1,0},{0,1},{0,-1}};
	static boolean[] arrive;
	static ArrayList<player> al = new ArrayList<Main.player>();
	static class player {
		int r, c;

		public player(int r, int c) {
			this.r = r;
			this.c = c;
		}

		@Override
		public String toString() {
			return "player [r=" + r + ", c=" + c + "]";
		}
	}
    public static void main(String[] args) throws IOException {
    	input();
    	while(K-->0) {
    		move();
    		turn();
    		
    		boolean flag = true;
    		for (int i = 0; i < M; i++) {
				if(!arrive[i]) {
					flag = false;
					break;
				}
			}
    		if(flag) break;
    	}
    	out_r++;
    	out_c++;
    	System.out.println(answer);
    	System.out.println(out_r + " " + out_c);
    }
    
	private static void turn() {
		ArrayList<player> copy = new ArrayList<Main.player>();
		for(int i=0; i<al.size(); i++) {
			if(arrive[i]) continue;
			player p = al.get(i);
			copy.add(new player(p.r, p.c));
		}
		Collections.sort(copy, (a,b) -> {
			if(a.r == b.r) return a.c - b.c;
			return a.r - b.r;
		});
		// 정사각형 한 변의 크기
		int size = 100, index = -1;
		for(int i=0; i<copy.size(); i++) {
			player p = copy.get(i);
			int dis1 = Math.max(Math.abs(out_r - p.r),Math.abs(out_c - p.c));
			if(size > dis1) {
				size = dis1;
				index = i;
			}
		}
		player p = copy.get(index);
		int left_r = Math.min(p.r, out_r), left_c = Math.min(p.c, out_c);
		int right_r = Math.max(p.r, out_r), right_c = Math.max(p.c, out_c);
		while(!(right_r - left_r == size)) {
			if(left_r - 1 < 0) right_r++;
			else left_r--;
		}
		while(!(right_c - left_c == size)) {
			if(left_c - 1 < 0) right_c++;
			else left_c--;
		}
		
		// 회전
		int[][] temp = new int[size+1][size+1];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp.length; j++) {
				temp[i][j] = arr[left_r+i][left_c+j];
			}
		}
		
		int r = 0, c = 0;
		for (int i = right_c; i >= left_c; i--) {
			for (int j = left_r; j <= right_r; j++) {
				arr[j][i] = temp[r][c];
				c++;
				if(c > size) {
					r++;
					c = 0;
				}
				// 벽 내구도 감소
				if(arr[j][i] > 0 && arr[j][i] <= 10) {
					arr[j][i]--;
				}
				// 출구처리
				else if(arr[j][i] == -1) {
					out_r = j;
					out_c = i;
				}
				// 사람처리
				else if(arr[j][i] > 10) {
					int tp = arr[j][i] >> 4, idx = 0;
					while(tp != 0) {
						if(tp%2 == 1) {
							player now = al.get(idx);
							now.r = j;
							now.c = i;
						}
						idx++;
						tp = tp >> 1;
					}
				}
			}
		}
		
	}

	private static void move() {
		for(int i=0; i<al.size(); i++) {
			if(arrive[i]) continue;
			player p = al.get(i);
			int row = out_r - p.r;
			int col = out_c - p.c;
			int nr = p.r, nc = p.c;
			boolean flag = true;
			if(row > 0 && p.r + 1 < N && (arr[p.r+1][p.c] <= 0 || arr[p.r+1][p.c] > 10)) {
				nr = p.r + 1;
			}
			else if(row < 0 && p.r - 1 >= 0 && (arr[p.r-1][p.c] <= 0 || arr[p.r-1][p.c] > 10)) {
				nr = p.r - 1;
			}
			else if(col > 0 && p.c + 1 < N && (arr[p.r][p.c+1] <= 0 || arr[p.r][p.c+1] > 10)) {
				nc = p.c + 1;
			}
			else if(col < 0 && p.c - 1 >= 0 && (arr[p.r][p.c-1] <= 0 || arr[p.r][p.c-1] > 10)) {
				nc = p.c - 1;
			}
			else flag = false;
			
			// 이동했다면
			if(flag) {
				answer++;
				// 이동한곳이 탈출구라면
				if(nr == out_r && nc == out_c) {
					arrive[i] = true;
					arr[p.r][p.c] -= 1<<(i+4);
				}
				else {
					arr[p.r][p.c] -= 1<<(i+4);
					arr[nr][nc] += 1<<(i+4);
					p.r = nr;
					p.c = nc;
				}
			}
			
		}
	}

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		arr = new int[N][N];
		arrive = new boolean[M];
		for (int i = 0; i < arr.length; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < arr.length; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			al.add(new player(r, c));
			arr[r][c] = 1<<(i+4);
		}
		st = new StringTokenizer(br.readLine());
		out_r = Integer.parseInt(st.nextToken()) - 1;
		out_c = Integer.parseInt(st.nextToken()) - 1;
		arr[out_r][out_c] = -1;
	}
}