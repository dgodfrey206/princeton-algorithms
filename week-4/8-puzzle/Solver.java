import java.util.LinkedList;
import java.util.Deque;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
  private Node solutionNode = null;
  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException("initial must not be null");
    }
    MinPQ<Node> tree1 = new MinPQ<Node>(
      (Node a, Node b) -> (a.board.manhattan() + a.moves - b.board.manhattan() - b.moves)
    );
    MinPQ<Node> tree2 = new MinPQ<Node>(
      (Node a, Node b) -> (a.board.manhattan() + a.moves - b.board.manhattan() - b.moves)
    );

    tree1.insert(new Node(initial));
    tree2.insert(new Node(initial.twin()));

    for (int i = 0; !tree1.isEmpty(); i++) {
      if (i % 2 == 0) {
        Node res = visit(tree1);
        if (res != null) {
          solutionNode = res;
          break;
        }
      } else {
        Node res = visit(tree2);
        if (res != null) {
          break;
        }
      }
    }
  }

  private Node visit(MinPQ<Node> pq) {
    if (!pq.isEmpty()) {
      Node curr = pq.delMin();
      if (curr.board.isGoal()) {
        return curr;
      }
      for (Board neighbor : curr.board.neighbors()) {
        if (curr.previous == null || !curr.previous.board.equals(neighbor)) {
          pq.insert(new Node(neighbor, curr.moves + 1, curr));
        }
      }
    }
    return null;
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return solutionNode != null;
  }

  // min number of moves to solve initial board
  public int moves() {
    if (solutionNode != null) {
      return solutionNode.moves;
    }
    return -1;
  }

  // sequence of boards in a shortest solution
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }
    Deque<Board> solutionPath = new LinkedList<Board>();
    Node node = solutionNode;
    while (node != null) {
      solutionPath.addFirst(node.board); 
      node = node.previous;
    }
    return solutionPath;
  }
 
  private class Node {
    public Board board = null;
    public Node previous = null;
    public int moves = 0;
    Node(Board board) {
      this.board = board;
    }
    Node(Board board, int moves, Node previous) {
      this.board = board;
      this.moves = moves;
      this.previous = previous;
    }
  }
}
