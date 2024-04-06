import java.io.*;
import java.util.*;

public class Main {
	static int n, m, answer;
	static int[][] arr;
	static int[][] dir = {{0,1},{1,0},{0,-1},{-1,0}};
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static class Dice {
		int r, c, under, up, right, front, left, back;

		public Dice(int r, int c, int under, int up, int right, int front, int left, int back) {
			this.r = r;
			this.c = c;
			this.under = under;
			this.up = up;
			this.right = right;
			this.front = front;
			this.left = left;
			this.back = back;
		}
		
		public void move(int d) {
			this.r += dir[d][0];
			this.c += dir[d][1];
			
			int under = this.under;
			int up = this.up;
			int right = this.right;
			int front = this.front;
			int left = this.left;
			int back = this.back;
			
			switch (d) {
			case 0:
				this.under = left;
				this.up = right;
				this.left = up;
				this.right = under;
				break;
				
			case 1:
				this.under = front;
				this.up = back;
				this.front = up;
				this.back = under;
				break;
				
			case 2:
				this.under = right;
				this.up = left;
				this.left = under;
				this.right = up;
				break;
				
			case 3:
				this.under = back;
				this.up = front;
				this.front = under;
				this.back = up;
				break;
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		input();
		answer = 0;
		Dice dice = new Dice(0, 0, 6, 1, 4, 2, 3, 5);
		int d = 0;
		while(m-->0) {
			// 주사위 이동방향으로 굴리기
			dice.move(d);
			
			
			// 점수 얻기
			getScore(dice.r, dice.c);
			
			// 다음 이동방향 정하기
			// 주사위 아랫면이 보드의 해당 칸보다 큰 경우 90도 시계
			if(dice.under > arr[dice.r][dice.c]) {
				d++;
				d %= 4;
			}
			// 작다면 90도 반시계
			else if(dice.under < arr[dice.r][dice.c]) {
				d += 3;
				d %= 4;
			}
			// 같다면 그대로 진행
			// 다음 이동이 격자판을 벗어난다면 방향 반대로
			int nr = dice.r + dir[d][0];
			int nc = dice.c + dir[d][1];
			if(nr<0||nc<0||nr>=n||nc>=n) {
				d += 2;
				d %= 4;
			}
		}
		System.out.print(answer);
	}

	private static void getScore(int r, int c) {
		int number = arr[r][c];
		boolean[][] visit = new boolean[n][n];
		Queue<int[]> q = new LinkedList<int[]>();
		q.add(new int[] {r, c});
		visit[r][c] = true;
		answer += arr[r][c];
		while(!q.isEmpty()) {
			int[] now = q.poll();
			
			for (int k = 0; k < 4; k++) {
				int nr = now[0] + dir[k][0];
				int nc = now[1] + dir[k][1];
				if(nr<0||nc<0||nr>=n||nc>=n) continue;
				if(visit[nr][nc]) continue;
				if(arr[nr][nc] != number) continue;
				q.add(new int[] {nr, nc});
				answer += arr[nr][nc];
				visit[nr][nc] = true;
			}
		}
	}

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n][n];
		for (int i = 0; i < arr.length; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < arr.length; j++) {
				arr[i][j] = Integer.parseInt(st.nextToken());
			}
		}
	}
}