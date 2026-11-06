public class GameManager {
    public enum State { PLAYING, CLEARED, TIME_OVER }

    private final MazeMap maze;
    private final Player player;

    private State state = State.PLAYING;
    private boolean bumped = false;
    private int timeLeftSec;

    public GameManager(MazeMap maze, Player player, int timeLimitSec) {
        this.maze = maze;
        this.player = player;
        this.timeLeftSec = timeLimitSec;
    }

    public void move(int dx, int dy) {
        if (state != State.PLAYING) return;

        int nx = player.getX() + dx;
        int ny = player.getY() + dy;

        if (nx < 0 || ny < 0 || nx >= maze.getWidth() || ny >= maze.getHeight()) {
            bumped = true;
            return;
        }
        if (maze.getCell(nx, ny) == 1) {
            bumped = true;
            return;
        }

        player.setPos(nx, ny);

        if (maze.getCell(nx, ny) == 2) {
            state = State.CLEARED;
        }
    }

    public void tick() {
        if (state != State.PLAYING) return;

        timeLeftSec--;
        if (timeLeftSec <= 0) {
            timeLeftSec = 0;
            state = State.TIME_OVER;
        }
    }

    public void restart(int timeLimitSec) {
        player.setPos(maze.getStartX(), maze.getStartY());
        state = State.PLAYING;
        bumped = false;
        timeLeftSec = timeLimitSec;
    }

    public boolean consumeBumped() {
        boolean was = bumped;
        bumped = false;
        return was;
    }

    public MazeMap getMaze() { return maze; }
    public Player getPlayer() { return player; }
    public State getState() { return state; }

    public boolean isCleared() { return state == State.CLEARED; }
    public boolean isTimeOver() { return state == State.TIME_OVER; }
    public int getTimeLeftSec() { return timeLeftSec; }
}
