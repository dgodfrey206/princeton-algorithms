import java.util.ArrayList;

public class Board {
  private int[][] board;
  private final int n;
  private int hammingDist;
  private int manhattanDist;
  private int blankX, blankY;
  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null) {
      throw new IllegalArgumentException("tiles must not be null");
    }
    n = tiles.length;
    board = new int[n][n];
    hammingDist = 0;
    manhattanDist = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        board[i][j] = tiles[i][j];
        if (board[i][j] == 0) {
          blankX = i;
          blankY = j;
        }
        else {
          int x = board[i][j] - 1;
          int deltaX = Math.abs(i - x / n);
          int deltaY = Math.abs(j - x % n);
          if (deltaX + deltaY > 0) {
            hammingDist++;
            manhattanDist += deltaX + deltaY;
          }
        }
      }
    }
  }
                                          
  // string representation of this board
  public String toString() {
    StringBuilder ret = new StringBuilder();
    ret.append(n);
    for (int i = 0; i < n; i++) {
      ret.append("\n");
      String sep = "";
      for (int j = 0; j < n; j++) {
        ret.append(sep + board[i][j]);
        sep = " ";
      }
    }
    return ret.toString();
  }

  // board dimension n
  public int dimension() {
    return n;
  }

  // number of tiles out of place
  public int hamming() {
    return hammingDist;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    return manhattanDist;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return hammingDist == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (this == y) {
      return true;
    }
    if (y == null) {
      return false;
    }
    if (getClass() != y.getClass()) {
      return false;
    }
    Board other = (Board) y;
    if (dimension() != other.dimension()) {
      return false;
    }
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (board[i][j] != ((Board) y).board[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    ArrayList<Board> boards = new ArrayList<Board>();
    int[] dx = {-1, 0, 1, 0};
    int[] dy = { 0, 1, 0, -1};
    for (int i = 0; i < 4; i++) {
      if (!(blankX + dx[i] < 0 || blankX + dx[i] >= n ||
          blankY + dy[i] < 0 || blankY + dy[i] >= n)) {
        swap(blankX, blankY, blankX + dx[i], blankY + dy[i]);
        boards.add(new Board(board));
        swap(blankX, blankY, blankX + dx[i], blankY + dy[i]);
      }
    }
    return boards;
  }
  

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    Board b = null;
    for (int i = 0; i < n; i++) {
      if (board[0][i] != 0 && board[n - 1][i] != 0) {
        swap(0, i, n - 1, i);
        b = new Board(board);
        swap(0, i, n - 1, i);
        break;
      }
    }
    return b;
  }

  private void swap(int a, int b, int c, int d) {
    int temp = board[a][b];
    board[a][b] = board[c][d];
    board[c][d] = temp;
  }
}
