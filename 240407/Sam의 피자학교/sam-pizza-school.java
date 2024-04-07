import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int n, k;
	static int[] arr;
	static int[][] dir = {{1,0},{0,1},{-1,0},{0,-1}};
	static ArrayList<Integer>[] al;
	
    public static void main(String[] args) throws IOException {
        int answer = 0;
    	input();
        while(true) {
        	answer++;
        	// 1. 밀가루 1만큼 더 넣어주기
        	add();
        	for (int i = 0; i < al.length; i++) {
    			al[i].clear();
    			al[i].add(arr[i]);
    		}
        	
        	// 2. 도우를 말아주기
        	roll();
        	
        	// 3. 도우를 꾹 눌러주기
        	push();
        	
        	// 4. 도우를 두 번 접기
        	snap();
        	
        	// 5. 3번 반복
        	push();
        	
        	int max = 0, min = 3001;
        	for (int i = 0; i < n; i++) {
				int num = al[i].get(0);
				max = Math.max(max, num);
				min = Math.min(min, num);
				arr[i] = num;
        	}
        	
        	if(max - min <= k) break;
        	
        	
        }
        
        System.out.println(answer);
    }
    
	private static void snap() {
		// 두 번 접기
		int tempN = n / 2, turn = 0, startIdx = 0;
		while (turn < 2) {
			turn++;
			int firstIdx = n - tempN;
			for (int i = startIdx + tempN-1; i >= startIdx; i--) {
				for (int j = al[i].size()-1; j >=0 ; j--) {
					al[firstIdx].add(al[i].get(j));
				}
				firstIdx++;
				al[i].clear();
			}
			startIdx += tempN;
			tempN /= 2;
		}
	}

	private static void push() {
		// 먼저 퍼지는거
		ArrayList<Integer>[] temp = new ArrayList[n];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = new ArrayList<>();
		}
		
		for (int i = 0; i < n; i++) {
			for(int j=0; j<al[i].size(); j++) {
				int sum = 0;
				for (int k = 0; k < 4; k++) {
					int nr = j + dir[k][0];
					int nc = i + dir[k][1];
					if(nc<0||nc>=n) continue;
					if(nr<0||al[nc].size() <= nr) continue;
					sum -= (al[i].get(j) - al[nc].get(nr)) / 5;
				}
				temp[i].add(sum + al[i].get(j));
			}
		}
		
		// 이제 누르기
		int index = 0;
		for (int i = 0; i < n; i++) {
			for(int j=0; j<temp[i].size(); j++) {
				al[index].clear();
				al[index++].add(temp[i].get(j));
			}
		}
		
		
	}

	private static void roll() {
		int startIdx = 0, rollWid = 1, rollHigh = 1;
		while(true) {
			int index = startIdx + rollWid;
			for (int i = 0; i < rollHigh; i++) {
				for (int j = rollWid - 1; j >= 0; j--) {
					int num = al[startIdx + j].get(i); 
					al[index].add(num);
				}
				index++;
			}
			for (int i = 0; i < rollWid; i++) {
				al[startIdx++].clear();
			}
			if(rollWid == rollHigh) rollHigh++;
			else rollWid++;
			
			if(startIdx + rollWid + rollHigh > n) break;
		}
	}

	private static void add() {
		int min = 3001;
		ArrayList<Integer> temp = new ArrayList<>();
		
		for (int i = 0; i < arr.length; i++) {
			if(min > arr[i]) {
				temp.clear();
				temp.add(i);
				min = arr[i];
			}
			else if(min == arr[i]) temp.add(i);
		}
		
		for(int t : temp) {
			arr[t]++;
		}
	}

	private static void input() throws IOException {
		st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		arr = new int[n];
		al = new ArrayList[n];
		for (int i = 0; i < al.length; i++) {
			al[i] = new ArrayList<>();
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
	}
}