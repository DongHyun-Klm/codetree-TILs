import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int L, N, Q;
	static int[][] arr, knights, temp, dir = {{-1,0},{0,1},{1,0},{0,-1}};
	static int[] ori_k;
	static boolean[] move, die;
	static boolean flag;
	static ArrayList<Knight> al = new ArrayList<>();
	static class Knight {
		int r, c, h, w, k;
		public Knight(int r, int c, int h, int w, int k) {
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
		}
	}
    public static void main(String[] args) throws IOException {
    	input();
    	int answer = 0;
    	while(Q-->0) {
    		st = new StringTokenizer(br.readLine());
    		int index = Integer.parseInt(st.nextToken()) - 1;
    		int d = Integer.parseInt(st.nextToken());
    		if(die[index]) continue;
    		temp = new int[L][L];
    		move = new boolean[N];
    		flag = true;
    		move(index ,d);
    		if(flag) {
    			for (int i = 0; i < N; i++) {
    				if(move[i]) continue;
    				Knight k = al.get(i);
    				for (int r = k.r; r < k.r + k.h; r++) {
						for (int c = k.c; c < k.c + k.w; c++) {
							temp[r][c] = knights[r][c];
						}
					}
    			}
    			for (int i = 0; i < L; i++) {
    				knights[i] = temp[i].clone();
				}
    			for (int i = 0; i < N; i++) {
					if(!move[i]) continue;
					Knight k = al.get(i);
					k.r += dir[d][0];
					k.c += dir[d][1];
					if(i==index) continue;
					int count = 0;
					for (int r = k.r; r < k.r + k.h; r++) {
						for (int c = k.c; c < k.c + k.w; c++) {
							if(arr[r][c] == 1) count++;
						}
					}
					k.k -= count;
					// 체력 다 소모한 경우
					if(k.k <= 0) {
						die[i] = true;
						for (int r = k.r; r < k.r + k.h; r++) {
							for (int c = k.c; c < k.c + k.w; c++) {
								knights[r][c] = 0;
							}
						}
					}
				}
    		}
    	}
    	for (int i = 0; i < N; i++) {
			if(die[i]) continue;
			answer += ori_k[i] - al.get(i).k;
		}
    	System.out.println(answer);
    }
    
	private static void move(int index, int d) {
		move[index] = true;
		Knight k = al.get(index);
		for (int r = k.r; r < k.r + k.h; r++) {
			for (int c = k.c; c < k.c + k.w; c++) {
				int nr = r + dir[d][0];
				int nc = c + dir[d][1];
				if(nr<0||nc<0||nr>=L||nc>=L||arr[nr][nc]==2) {
					flag = false;
					return;
				}
				if(knights[nr][nc] != index+1 && knights[nr][nc]>0) move(knights[nr][nc]-1, d);
				if(!flag) return;
				temp[nr][nc] = knights[r][c];
			}
		}
		
	}

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		arr = new int[L][L];
		knights = new int[L][L];
		die = new boolean[N];
		ori_k = new int[N];
		for (int i = 0; i < L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < L; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			al.add(new Knight(r, c, h, w, k));
			ori_k[i] = k;
			for (int j = r; j < r+h; j++) {
				for (int j2 = c; j2 < c+w; j2++) {
					knights[j][j2] = i+1;
				}
			}
		}
	}
}