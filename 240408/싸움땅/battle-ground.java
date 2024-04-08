import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int n, m, k;
	static PriorityQueue<Integer>[][] gun;
	static ArrayList<player> players = new ArrayList<>();
	static ArrayList<Integer>[][] personNum;
	static int[][] dir = {{-1,0},{0,1},{1,0},{0,-1}};
	
	static class player {
		int r, c, d, s, point, gun;

		@Override
		public String toString() {
			return "player [r=" + r + ", c=" + c + ", d=" + d + ", s=" + s + ", point=" + point + ", gun=" + gun + "]";
		}

		public player(int r, int c, int d, int s, int point, int gun) {
			super();
			this.r = r;
			this.c = c;
			this.d = d;
			this.s = s;
			this.point = point;
			this.gun = gun;
		}

		public void move() {
			int nr = r + dir[d][0];
			int nc = c + dir[d][1];
			// 격자를 벗어나면 반대 방향
			if(nr<0||nc<0||nr>=n||nc>=n) {
				d += 2;
				d %= 4;
				nr = r + dir[d][0];
				nc = c + dir[d][1];
			}
			personNum[r][c].clear();
			this.r = nr;
			this.c = nc;
		}
	}
	
    public static void main(String[] args) throws IOException {
        input();
        
        
        while(k-->0) {
        	for(int i=0; i<players.size(); i++) {
        		player p = players.get(i);
        		
            	// 1-1 플레이어들 한 칸 이동
            	p.move();
            	personNum[p.r][p.c].add(i+1);
            	
            	
            	// 2-1 이동한 방향에 플레이어가 없다면 총 획득(쎈 총)
            	if(!gun[p.r][p.c].isEmpty()) {
            		if(personNum[p.r][p.c].size() == 1) {
                		if(p.gun == 0) {
                			p.gun = gun[p.r][p.c].poll();
                		}
                		else if(p.gun < gun[p.r][p.c].peek()) {
                			int temp = p.gun;
                			p.gun = gun[p.r][p.c].poll();
                			gun[p.r][p.c].add(temp);
                		}
                	}
            	}
            	
            	// 2-2-1 플레이어가 있다면 싸움
            	if(personNum[p.r][p.c].size() > 1) {
            		player p1 = players.get(personNum[p.r][p.c].get(0) - 1);
            		player p2 = players.get(personNum[p.r][p.c].get(1) - 1);
            		boolean p1Win = false;
            		if(p1.s + p1.gun > p2.s + p2.gun || (p1.s + p1.gun == p2.s + p2.gun && p1.s > p2.s) ) {
            			p1Win = true;
            		}
            		if(!p1Win) {
            			p2 = players.get(personNum[p.r][p.c].get(0) - 1);
                		p1 = players.get(personNum[p.r][p.c].get(1) - 1);
            		}
            		// p1이 승자
            		p1.point += (p1.s + p1.gun) - (p2.s + p2.gun);
            		
            		// 2-2-2 진 플레이어는 총을 내려놓고, 원래 방향 한칸 이동 후 총 획득
            		if(p2.gun != 0) {
            			gun[p2.r][p2.c].add(p2.gun);
            			p2.gun = 0;
            		}
            		for (int k = 0; k < 4; k++) {
						int nr = p2.r + dir[(p2.d+k)%4][0];
						int nc = p2.c + dir[(p2.d+k)%4][1];
						if(nr<0||nc<0||nr>=n||nc>=n) continue;
						if(personNum[nr][nc].size() != 0) continue;
						if(p1Win) {
							personNum[nr][nc].add(personNum[p.r][p.c].get(1));
							personNum[p.r][p.c].remove(1);
						}
						else {
							personNum[nr][nc].add(personNum[p.r][p.c].get(0));
							personNum[p.r][p.c].remove(0);
						}
						p2.r = nr;
						p2.c = nc;
						p2.d += k;
						p2.d %= 4;
						break;
					}
            		
            		if(!gun[p2.r][p2.c].isEmpty()) p2.gun = gun[p2.r][p2.c].poll();
            		
            		// 2-2-3 이긴 플레이어는 공격력이 가장 높은 총 획득
            		if(!gun[p1.r][p1.c].isEmpty()) {
            			if(p1.gun == 0) {
                    		p1.gun = gun[p1.r][p1.c].poll();
                    	}
                    	else if(p1.gun < gun[p1.r][p1.c].peek()) {
                    		int temp = p1.gun;
                    		p1.gun = gun[p1.r][p1.c].poll();
                    		gun[p1.r][p1.c].add(temp);
                    	}
            		}
            	}
            	
            }
        }
        for(player p : players) {
        	System.out.print(p.point + " ");
        }
    }

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		gun = new PriorityQueue[n][n];
		personNum = new ArrayList[n][n];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				gun[i][j] = new PriorityQueue<>((a,b)-> b - a);
				personNum[i][j] = new ArrayList<Integer>();
				int now = Integer.parseInt(st.nextToken());
				if(now != 0) gun[i][j].add(now);
			}
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			players.add(new player(r, c, d, s, 0, 0));
			personNum[r][c].add(i+1);
		}
		
	}
}