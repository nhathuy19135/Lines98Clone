package Lines;

import java.util.Random;

public class Lineball {

	public final int MaxCell = 9;
	public final int MaxColor = 7;
	public int[][] ball = new int[MaxCell][MaxCell];
	public int[][] balltmp = new int[MaxCell][MaxCell];
	public Point[] PathBall = new Point[MaxCell * MaxCell];
	public int[] nextcolor = new int[3];
	public int[] nextcolortmp = new int[3];
	public int nCountPath;
	public double MarkResult;// Total Score
	public double MarkResultTemp;// Total score after everymove
	boolean gameover;

	//------------------------------------------------------------------
	public Lineball() {
	}

	public void StartGame() {
		// Initialize the board

		for (int i = 0; i < MaxCell; i++)
			for (int j = 0; j < MaxCell; j++)
				ball[i][j] = 0;

		MarkResult = 0;
		gameover = false;

		// Place the first 6 balls
		int i, j;
		Point[] FreeCell = new Point[3];
		Random random = new Random();

		// Place 3 big balls
		if (RandomBall(3, FreeCell))
			for (int k = 0; k < 3; k++) {

				i = FreeCell[k].x;
				j = FreeCell[k].y;
				ball[i][j] = random.nextInt(MaxColor) + 1;
			}
		else System.out.println("Game over!");

		// Place 3 small balls
		if (RandomBall(3, FreeCell))
			for (int k = 0; k < 3; k++) {

				i = FreeCell[k].x;
				j = FreeCell[k].y;
				ball[i][j] = random.nextInt(MaxColor) + MaxColor + 1;
			}
		else System.out.println("Game over!");

		//Save board
		for (i = 0; i < MaxCell; i++)
			for (j = 0; j < MaxCell; j++)
				balltmp[i][j] = ball[i][j];
		//Generate 3 random colors
		new3color();
		System.arraycopy(nextcolor, 0, nextcolortmp, 0, 3);
	}

	//-------------------------------------------------------------------
	public void new3color() {  //Create 3 colors
		Random random = new Random();
		for (int k = 0; k < 3; k++)
			nextcolor[k] = random.nextInt(MaxColor) + 1;
	}

	public void new3Balls() {
		// Replace small balls with big balls
		for (int i = 0; i < MaxCell; i++)
			for (int j = 0; j < MaxCell; j++)
				if (ball[i][j] > MaxColor)
					ball[i][j] -= (MaxColor);

		// Create 3 small balls with random colors
		int i, j;
		Point[] FreeCell = new Point[3];
		if (RandomBall(3, FreeCell))
			for (int k = 0; k < 3; k++) {

				i = FreeCell[k].x;
				j = FreeCell[k].y;
				ball[i][j] = nextcolor[k] + MaxColor;
			}
		else gameover = true;
		new3color();
	}

	//-------------------------------------------------------------------
	//Choose 3 random blank slots, then place small balls into that slot
	public boolean RandomBall(int nBall, Point[] ResultBall) {
		int ncountFreeBall = 0;
		Point[] CheckCell = new Point[MaxCell * MaxCell];
		boolean[] BoolCheckCell = new boolean[MaxCell * MaxCell];

		for (int i = 0; i < MaxCell; i++)
			for (int j = 0; j < MaxCell; j++)
				if ((ball[i][j]) == 0) {// check if there is already a ball in that slot

					CheckCell[ncountFreeBall] = new Point(i, j);
					BoolCheckCell[ncountFreeBall] = true;
					ncountFreeBall++;

				} else
					BoolCheckCell[ncountFreeBall] = false;
		if (ncountFreeBall < nBall) return false;

		Random random = new Random();
		int x;
		int nCount = 0;
		while (nCount < nBall) {

			x = random.nextInt(ncountFreeBall);//randomly choose 1 empty slot
			if (BoolCheckCell[x]) {

				BoolCheckCell[x] = false;
				ResultBall[nCount++] = CheckCell[x];

			}
		}
		return true;
	}

	//--------------------------------------------------------------------
	//The undo feature
	public void Undo() {

		for (int i = 0; i < MaxCell; i++)
			System.arraycopy(balltmp[i], 0, ball[i], 0, MaxCell);

		System.arraycopy(nextcolortmp, 0, nextcolor, 0, 3);

		MarkResult = MarkResultTemp;
	}

	//-------------------------------------------------------------------
	//Save previous board state
	public void saveUndo() {

		for (int i = 0; i < MaxCell; i++)
			for (int j = 0; j < MaxCell; j++)
				if (ball[i][j] > 2 * MaxColor)
					balltmp[i][j] = ball[i][j] - 2 * MaxColor;
				else
					balltmp[i][j] = ball[i][j];
		System.arraycopy(nextcolor, 0, nextcolortmp, 0, 3);

		MarkResultTemp = MarkResult;

	}

	public boolean cutBall() {
		int NumCutBall = 0;
		int nBall;
		boolean[][] CheckBall = new boolean[MaxCell][MaxCell];
		Point[] TempBall = new Point[MaxCell];
		Point[] CellBall = new Point[MaxCell * MaxCell];
		int i, j, nRow, nCol, nCount;

		for (i = 0; i < MaxCell; i++)
			for (j = 0; j < MaxCell; j++)
				CheckBall[i][j] = false;

		for (nRow = 0; nRow < MaxCell; nRow++)
			for (nCol = 0; nCol < MaxCell; nCol++)
				if (ball[nRow][nCol] > 0 && !CheckBall[nRow][nCol]) {

					nBall = ball[nRow][nCol];
					//check vertical
					i = nRow;
					j = nCol;
					while (i > 0 && ball[i - 1][j] == nBall)
						i--;
					nCount = 0;
					while (i < MaxCell && ball[i][j] == nBall) {

						CheckBall[i][j] = true;
						TempBall[nCount++] = new Point(i, j);
						i++;

					}
					if (nCount >= 5) {
						for (i = 0; i < nCount; i++)
							CellBall[NumCutBall++] = TempBall[i];

						MarkResult += (nCount - 4) * nCount;

					}

					//check horizontal
					i = nRow;
					while (j > 0 && ball[i][j - 1] == nBall)
						j--;
					nCount = 0;
					while (j < MaxCell && ball[i][j] == nBall) {

						CheckBall[i][j] = true;
						TempBall[nCount++] = new Point(i, j);
						j++;

					}
					if (nCount >= 5) {
						for (i = 0; i < nCount; i++)
							CellBall[NumCutBall++] = TempBall[i];

						MarkResult += (nCount - 4) * nCount;

					}

					//check left diagonal
					i = nRow;
					j = nCol;
					while (i > 0 && j > 0 && ball[i - 1][j - 1] == nBall) {

						i--;
						j--;

					}
					nCount = 0;
					while (i < MaxCell && j < MaxCell && ball[i][j] == nBall) {

						CheckBall[i][j] = true;
						TempBall[nCount++] = new Point(i, j);
						i++;
						j++;

					}
					if (nCount >= 5) {
						for (i = 0; i < nCount; i++)
							CellBall[NumCutBall++] = TempBall[i];

						MarkResult += (nCount - 4) * nCount;

					}
					//check right diagonal
					i = nRow;
					j = nCol;
					while (i > 0 && j + 1 < MaxCell && ball[i - 1][j + 1] == nBall) {

						i--;
						j++;

					}
					nCount = 0;
					while (i < MaxCell && j >= 0 && ball[i][j] == nBall) {

						CheckBall[i][j] = true;
						TempBall[nCount++] = new Point(i, j);
						i++;
						j--;

					}
					if (nCount >= 5) {
						for (i = 0; i < nCount; i++)
							CellBall[NumCutBall++] = TempBall[i];

						MarkResult += (nCount - 4) * nCount;

					}

				}
		for (i = 0; i < NumCutBall; i++)
			ball[CellBall[i].x][CellBall[i].y] = 0;
		return NumCutBall > 0;

	}

	//-------------------------------------------------------------------
	//check 5 balls
	//-------------------------------------------------------------------
	//Save path
	public void FindPath(Point p, Point[][] PathBallTemp) {
		if (p.x != -1 && p.y != -1)
			if (!PathBallTemp[p.x][p.y].equals(new Point(-1, -1)))
				FindPath(PathBallTemp[p.x][p.y], PathBallTemp);
		PathBall[nCountPath++] = p;
	}

	//-------------------------------------------------------------------
	//Load path
	public boolean Load(int si, int sj, int fi, int fj) { // load to find path: (si,sj)-->(fi,fj)
		int[] di = {-1, 1, 0, 0};
		int[] dj = {0, 0, -1, 1};
		int i, j, k, nCount;
		Point pStart, pCurrent;
		Point[][] Query = new Point[2][MaxCell * MaxCell];
		Point[][] PathBallTemp = new Point[MaxCell][MaxCell];
		boolean[][] ballCheck = new boolean[MaxCell][MaxCell];

		pStart = new Point(si, sj);//Starting point from user's mouse click

		//put pStart to Query
		int nQuery = 1;
		Query[0][0] = pStart;

		//mark cells that already have the ball
		for (i = 0; i < MaxCell; i++)
			for (j = 0; j < MaxCell; j++)
				ballCheck[i][j] = ball[i][j] > 0 && ball[i][j] < 8;

		ballCheck[pStart.x][pStart.y] = true;
		if (ballCheck[fi][fj])
			return false;
		//DFS
		PathBallTemp[si][sj] = new Point(-1, -1);
		while (nQuery > 0) {
			nCount = 0;
			for (int nLast = 0; nLast < nQuery; nLast++) {
				pCurrent = Query[0][nLast];
				//Find in 4 direction (horizontal, vertical, left diagonal, right diagonal)
				for (k = 0; k < 4; k++) {
					i = pCurrent.x + di[k];
					j = pCurrent.y + dj[k];
					if (i >= 0 && i < MaxCell && j >= 0 & j < MaxCell && !ballCheck[i][j]) {

						Query[1][nCount++] = new Point(i, j);
						ballCheck[i][j] = true;
						PathBallTemp[i][j] = new Point(pCurrent.x, pCurrent.y);
						//If found, stop
						if (ballCheck[fi][fj]) {
							nCountPath = 0;
							FindPath(new Point(fi, fj), PathBallTemp);

							return true;
						}

					}
				}
			}
			for (k = 0; k < nCount; k++)
				Query[0][k] = Query[1][k];
			nQuery = nCount;
		}

		return false;
	}


}