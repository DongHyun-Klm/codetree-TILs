import java.util.*;
import java.io.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static int N, M, K;
    static int[][] arr;
    static int[][] atk;
    static boolean flag_razer;
    static ArrayList<tower> towers = new ArrayList<Main.tower>();
    static class info {
        int r, c;
        String route;
        
        info(int r, int c, String route){
            this.r = r;
            this.c = c;
            this.route = route;
        }
        @Override
        public String toString(){
            return r + " " + c + " " + route;
        }
    }
    static class tower implements Comparable<tower>{
    	int damage, turn, rc, c;

		public tower(int damage, int turn, int rc, int c) {
			this.damage = damage;
			this.turn = turn;
			this.rc = rc;
			this.c = c;
		}

		@Override
		public int compareTo(tower o) {
			if(this.damage == o.damage) {
				if(this.turn == o.turn) {
					if(this.rc == o.rc) {
						return o.c - this.c;
					} else return o.rc - this.rc;
				} else return this.turn - o.turn;
			} else return this.damage - o.damage;
		}
    }
    
    public static void main(String[] args) throws IOException {
        input();
        int answer = 0;
        while(K-->0) {
            answer = 0;
            flag_razer = false;
            towers.clear();
            for(int i=0; i<N; i++) {
                for(int j=0; j<M; j++) {
                	if(arr[i][j] != 0) towers.add(new tower(arr[i][j], atk[i][j], i+j, j));
                }
            }
            Collections.sort(towers);
            
            boolean[][] ass = new boolean[N][M];
            // 공격자 선정
            int[] pik_atk = new int[] {towers.get(0).rc - towers.get(0).c, towers.get(0).c};
            ass[pik_atk[0]][pik_atk[1]] = true;
            
            // 피공격자 선정
            int len = towers.size() - 1;
            int[] pik_str = new int[] {towers.get(len).rc - towers.get(len).c, towers.get(len).c};
            ass[pik_str[0]][pik_str[1]] = true;

            arr[pik_atk[0]][pik_atk[1]] += N+M;

            // 레이저 공격
            razer(pik_atk, pik_str, ass);

            // 포탄 공격(레이저 공격 실패시)
            if(!flag_razer) bomb(pik_atk, pik_str, ass);

            // 공격 기록
            for(int i=0; i<N; i++) {
                for(int j=0; j<M; j++) {
                    if(i==pik_atk[0] && j==pik_atk[1]) atk[i][j] = 0;
                    else atk[i][j]++;
                }
            }

            // 포탑 부서짐
            for(int i=0; i<N; i++) {
                for(int j=0; j<M; j++) {
                    if(arr[i][j] < 0) arr[i][j] = 0;
                }
            }

            

            // 포탑 정비
            int cnt = 0;

            for(int i=0; i<N; i++) {
                for(int j=0; j<M; j++) {
                    if(arr[i][j] != 0 && !ass[i][j]) arr[i][j]++;
                    if(arr[i][j] != 0) {
                        cnt++;
                        answer = Math.max(answer, arr[i][j]);
                    }
                }
            }

            // 부서지지 않은 포탑이 1개라면 중지
            if(cnt == 1) break;
        }

        System.out.print(answer);
    }

    static private void razer(int[] pick_atk, int[] pick_str, boolean[][] ass) {
        int[][] dir = new int[][] {{0,1},{1,0},{0,-1},{-1,0}};
        boolean[][] visit = new boolean[N][M];
        ArrayList<int[]> al = new ArrayList<>();
        Queue<info> q = new LinkedList<>();
        PriorityQueue<String> pq = new PriorityQueue<>((a,b) -> {
            char[] cr1 = a.toCharArray();
            char[] cr2 = b.toCharArray();
            for(int i=0; i<cr1.length; i++) {
                if(cr1[i] != cr2[i]) return cr1[i] - cr2[i];
            }
            return 1;
        });
        q.add(new info(pick_atk[0], pick_atk[1], ""));
        visit[pick_atk[0]][pick_atk[1]] = true;

        while(!q.isEmpty()) {
            int size = q.size();
            al.clear();
            while(size-->0) {
                info now = q.poll();
                for(int k=0; k<4; k++) {
                    int nr = (now.r + dir[k][0]) % N;
                    int nc = (now.c + dir[k][1]) % M;
                    if(nr<0) nr += N;
                    if(nc<0) nc += M;
                    if(arr[nr][nc] == 0) continue;
                    if(visit[nr][nc]) continue;
                    q.add(new info(nr, nc, now.route + k));
                    al.add(new int[] {nr,nc});
                    if(nr == pick_str[0] && nc == pick_str[1]) {
                        flag_razer = true;
                        pq.add(now.route + k);
                    }
                }
            }
            if(flag_razer) break;
            for(int[] t : al) {
                visit[t[0]][t[1]] = true;
            }
        }
        
        if(!flag_razer) return;
        char[] ar = pq.poll().toCharArray();
        int damage = arr[pick_atk[0]][pick_atk[1]];
        int r = pick_atk[0], c = pick_atk[1];
        for(int i=0; i<ar.length; i++) {
            int k = ar[i] - '0';
            r += dir[k][0];
            c += dir[k][1];
            r %= N;
            c %= M;
            if(r<0) r += N;
            if(c<0) c += M;
            if(i == ar.length-1) arr[r][c] -= damage;
            else {
                arr[r][c] -= damage/2;
                ass[r][c] = true;
            }
        }
    }

    static private void bomb(int[] pick_atk, int[] pick_str, boolean[][] ass) {
        int[][] dir = new int[][] {{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1},{0,1},{1,1}};
        int r = pick_str[0], c = pick_str[1];
        int damage = arr[pick_atk[0]][pick_atk[1]];
        arr[r][c] -= damage;
        for(int k=0; k<8; k++) {
            int nr = (r + dir[k][0]) % N;
            int nc = (c + dir[k][1]) % M;
            if(nr<0) nr += N;
            if(nc<0) nc += M;
            if(nr == pick_atk[0] && nc == pick_atk[1]) continue;
            arr[nr][nc] -= damage/2;
            ass[nr][nc] = true;
        }
    }
    
    static private void input() throws IOException {
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        arr = new int[N][M];
        atk = new int[N][M];
        // 시점0에 모두 공격
        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<M; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}