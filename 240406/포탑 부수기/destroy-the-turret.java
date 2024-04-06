import java.util.*;
import java.io.*;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static int N, M, K;
    static int[][] arr;
    static int[][] atk;
    static boolean flag_razer;
    static class info{
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
    
    public static void main(String[] args) throws IOException {
        input();
        int answer = 0;
        while(K-->0) {
            answer = 0;
            flag_razer = false;
            boolean[][] ass = new boolean[N][M];
            // 공격자 선정
            int[] pik_atk = pick_at();
            ass[pik_atk[0]][pik_atk[1]] = true;
            
            // 피공격자 선정
            int[] pik_str = pick_st();
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

    // 공격자 선정
    static private int[] pick_at() {
        ArrayList<int[]> al = new ArrayList<>();
        int min = 5001;
        // 공격력 가장 낮은 포탑 찾기
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(arr[i][j] != 0 && arr[i][j] < min) {
                    al.clear();
                    al.add(new int[] {i,j});
                    min = arr[i][j];
                }
                else if(arr[i][j] == min) al.add(new int[] {i,j});
            }
        }
        if(al.size()==1) return al.get(0);

        // 최근에 공격한 포탑 찾기
        int cnt = 0, size = al.size();
        min = 1001;
        for(int[] t : al) {
            min = Math.min(atk[t[0]][t[1]], min); 
        }
        for(int i=0; i<size; i++) {
            int[] t = al.get(i);
            if(atk[t[0]][t[1]] == min) cnt++;
        }
        if(cnt != size) {
            for(int i=0; i<al.size(); i++) {
                int[] t = al.get(i);
                if(atk[t[0]][t[1]] != min) {
                    al.remove(i);
                    i--; // 여기 다시확인하기
                }
            }
        }
        if(al.size()==1) return al.get(0);

        // 행과 열의 합이 가장 큰 포탑 찾기 >> 내 맘대로 해석해버림
        int[] sum = new int[al.size()];
        int max = 0;
        for(int i=0; i<sum.length; i++) {
            int[] t = al.get(i);
            sum[i] = t[0] + t[1];
            max = Math.max(max, sum[i]);
        }
        ArrayList<int[]> al2 = new ArrayList<>();
        for(int i=0; i<sum.length; i++) {
            if(sum[i] == max) {
                al2.add(al.get(i));
            } 
        }
        if(al2.size()==1) return al2.get(0);

        // 열 값이 가장 큰 포탑 찾기
        sum = new int[al2.size()];
        max = 0;
        for(int i=0; i<sum.length; i++) {
            int[] t = al2.get(i);
            sum[i] = t[1];
            max = Math.max(max, sum[i]);
        }
        
        ArrayList<int[]> al3 = new ArrayList<>();
        for(int i=0; i<al2.size(); i++) {
            if(sum[i] == max) {
                al3.add(al2.get(i));
            } 
        }
        return al3.get(0);
    }

    // 피공격자 선정
    static private int[] pick_st() {
        ArrayList<int[]> al = new ArrayList<>();
        int max = 0;
        // 공격력 가장 높은 포탑 찾기
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                if(arr[i][j] != 0 && arr[i][j] > max) {
                    al.clear();
                    al.add(new int[] {i,j});
                    max = arr[i][j];
                }
                else if(arr[i][j] == max) al.add(new int[] {i,j});
            }
        }

        if(al.size()==1) return al.get(0);


        // 공격한지 가장 오래된 포탑 찾기
        int cnt = 0, size = al.size();
        max = 0;
        for(int[] t : al) {
            max = Math.max(atk[t[0]][t[1]], max); 
        }
        for(int i=0; i<size; i++) {
            int[] t = al.get(i);
            if(atk[t[0]][t[1]] == max) cnt++;
        }
        if(cnt != size) {
            for(int i=0; i<al.size(); i++) {
                int[] t = al.get(i);
                if(atk[t[0]][t[1]] != max) {
                    al.remove(i);
                    i--; // 여기 다시확인하기
                }
            }
        }
        if(al.size()==1) return al.get(0);

        // 행과 열의 합이 가장 작은 포탑 찾기
        int[] sum = new int[al.size()];
        int min = Integer.MAX_VALUE;
        for(int i=0; i<sum.length; i++) {
            int[] t = al.get(i);
            sum[i] = t[0] + t[1];
            min = Math.min(min, sum[i]);
        }
        ArrayList<int[]> al2 = new ArrayList<>();
        for(int i=0; i<sum.length; i++) {
            if(sum[i] == min) {
                al2.add(al.get(i));
            } 
        }
        if(al2.size()==1) return al2.get(0);

        // 열 값이 가장 작은 포탑 찾기
        sum = new int[al2.size()];
        min = Integer.MAX_VALUE;
        for(int i=0; i<sum.length; i++) {
            int[] t = al2.get(i);
            sum[i] = t[1];
            min = Math.min(min, sum[i]);
        }
        
        ArrayList<int[]> al3 = new ArrayList<>();
        for(int i=0; i<al2.size(); i++) {
            if(sum[i] == min) {
                al3.add(al2.get(i));
            } 
        }
        return al3.get(0);
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