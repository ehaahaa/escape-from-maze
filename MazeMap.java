public class MazeMap {
    // 0=길, 1=벽, 2=탈출구
    private final int[][] map;
    private final int startX;
    private final int startY;

    public enum Difficulty { EASY, NORMAL, HARD }

    private static final int[][] EASY = {
            {1,1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,0,0,1},
            {1,0,1,0,1,0,1,0,1},
            {1,0,1,0,0,0,1,2,1},
            {1,0,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1}
    };

    private static final int[][] NORMAL = {
            {1,1,1,1,1,1,1,1,1},
            {1,0,0,1,0,0,0,0,1},
            {1,1,0,1,0,1,1,0,1},
            {1,0,0,0,0,1,0,0,1},
            {1,0,1,1,0,1,0,1,1},
            {1,0,0,1,0,0,0,2,1},
            {1,1,1,1,1,1,1,1,1}
    };

    private static final int[][] HARD = {
            {1,1,1,1,1,1,1,1,1},
            {1,0,0,1,0,0,1,0,1},
            {1,0,1,1,0,1,1,0,1},
            {1,0,0,0,0,0,1,0,1},
            {1,1,1,0,1,0,1,0,1},
            {1,0,0,0,1,0,0,2,1},
            {1,1,1,1,1,1,1,1,1}
    };

    public MazeMap(Difficulty diff) {
        if (diff == Difficulty.EASY) map = EASY;
        else if (diff == Difficulty.NORMAL) map = NORMAL;
        else map = HARD;

        startX = 1;
        startY = 1;
    }

    public int[][] getMap() { return map; }
    public int getCell(int x, int y) { return map[y][x]; }

    public int getWidth() { return map[0].length; }
    public int getHeight() { return map.length; }

    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
}
