import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int n, m, k;
	static int[][] arr, wall, cool, temp;
	static int[][] dir = {{0,-1},{-1,0},{0,1},{1,0}};
	static boolean[][] upWall, leftWall, visit;
	static ArrayList<air> al = new ArrayList<>();
	static class air {
		int r, c, d;

		public air(int r, int c, int d) {
			this.r = r;
			this.c = c;
			this.d = d;
		}
	}
	
    public static void main(String[] args) throws IOException {
        input();
        int answer = 0;
        cool = new int[n][n];
        while(answer <= 100) {
        	answer++;
        	// 에어컨의 바람이 나와 공기를 시원하게 한다
        	for(air a : al) {
        		temp = new int[n][n];
        		visit = new boolean[n][n];
        		operation(a.r + dir[a.d][0], a.c + dir[a.d][1], a.d, 5);
        		for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						cool[i][j] += temp[i][j];
					}
				}
        	}
        	
        	
        	// 시원한 공기들이 섞이기 시작
        	mix();
        	
        	// 외벽에 있는 칸 1씩 감소
        	for (int i = 0; i < n; i++) {
    			for (int j = 0; j < n; j++) {
    				if(i == 0 || i == n-1 || j == 0 || j == n-1) {
    					if(cool[i][j] != 0) cool[i][j]--;
    				}
    			}
        	}
        	
        	// 사무실 시원해 졌는지 체크
        	boolean flag = true;
        	for (int i = 0; i < n; i++) {
    			for (int j = 0; j < n; j++) {
    				if(arr[i][j] == 1) {
    					if(cool[i][j] < k) flag = false;
    				}
    			}
        	}
        	if(flag) break;
        }
        
        if(answer == 101) answer = -1;
        System.out.println(answer);
    }

	

	private static void mix() {
		temp = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 각 칸마다 오른쪽과 아래 확산하는지 확인
				// 오른쪽
				int nc = j + 1;
				if(nc<n && !leftWall[i][nc] && Math.abs(cool[i][j] - cool[i][nc]) >= 4) {
					temp[i][j] -= (cool[i][j] - cool[i][nc]) / 4;
					temp[i][nc] -= (cool[i][nc] - cool[i][j]) / 4;
				}
				
				// 아래
				int nr = i + 1;
				if(nr<n && !upWall[nr][j] && Math.abs(cool[i][j] - cool[nr][j]) >= 4) {
					temp[i][j] -= (cool[i][j] - cool[nr][j]) / 4;
					temp[nr][j] -= (cool[nr][j] - cool[i][j]) / 4;
				}
			}
		}
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				cool[i][j] += temp[i][j];
				if(cool[i][j] < 0) cool[i][j] = 0;
			}
		}
	}



	private static void operation(int r, int c, int d, int power) {
		temp[r][c] = power;
		visit[r][c] = true;
		if(power == 1) return;
		
		// 3가지 방향
		// 1.가던방향 그대로
		int nr = r + dir[d][0], nc = c + dir[d][1];
		if(nr>=0 && nc>=0 && nr<n && nc<n && !visit[nr][nc]) {
			if(!(d == 0 && leftWall[r][c]) && !(d == 1 && upWall[r][c]) &&
					!(d == 2 && leftWall[nr][nc]) && !(d == 3 && upWall[nr][nc])) {
				operation(nr, nc, d, power-1);
			}
		}
		// 2. 가던방향 좌
		int nd = (d + 3) % 4;
		int nr2 = nr + dir[nd][0], nc2 = nc + dir[nd][1];
		if(nr2>=0 && nc2>=0 && nr2<n && nc2<n && !visit[nr2][nc2]) {
			int temp_r = r + dir[nd][0], temp_c = c + dir[nd][1];
			if(!(nd == 0 && leftWall[r][c]) && !(nd == 1 && upWall[r][c]) &&
					!(nd == 2 && leftWall[r][c+1]) && !(nd == 3 && upWall[r+1][c])) {
				if(!(d == 0 && leftWall[temp_r][temp_c]) && !(d == 1 && upWall[temp_r][temp_c]) &&
						!(d == 2 && leftWall[temp_r][temp_c + 1]) && !(d == 3 && upWall[temp_r + 1][temp_c])) {
					operation(nr2, nc2, d, power-1);
				}
			}
		}
		
		// 3. 가던방향 우
		nd = (d + 1) % 4;
		nr2 = nr + dir[nd][0];
		nc2 = nc + dir[nd][1];
		if(nr2>=0 && nc2>=0 && nr2<n && nc2<n && !visit[nr2][nc2]) {
			int temp_r = r + dir[nd][0], temp_c = c + dir[nd][1];
			if(!(nd == 0 && leftWall[r][c]) && !(nd == 1 && upWall[r][c]) &&
					!(nd == 2 && leftWall[r][c+1]) && !(nd == 3 && upWall[r+1][c])) {
				if(!(d == 0 && leftWall[temp_r][temp_c]) && !(d == 1 && upWall[temp_r][temp_c]) &&
						!(d == 2 && leftWall[temp_r][temp_c + 1]) && !(d == 3 && upWall[temp_r + 1][temp_c])) {
					operation(nr2, nc2, d, power-1);
				}
			}
		}
	}



	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		arr = new int[n][n];
		wall = new int[m][3];
		upWall = new boolean[n][n];
		leftWall = new boolean[n][n];
		for (int i = 0; i < arr.length; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < arr.length; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
				if(arr[i][j] > 1) al.add(new air(i, j, arr[i][j] - 2));
			}
		}
		
		for (int i = 0; i < wall.length; i++) {
			st = new StringTokenizer(br.readLine());
			wall[i][0] = Integer.parseInt(st.nextToken()) - 1;
			wall[i][1] = Integer.parseInt(st.nextToken()) - 1;
			wall[i][2] = Integer.parseInt(st.nextToken());
			
			if(wall[i][2] == 0) {
				upWall[wall[i][0]][wall[i][1]] = true;
			}
			else if(wall[i][2] == 1) {
				leftWall[wall[i][0]][wall[i][1]] = true;
			}
		}
	}
}