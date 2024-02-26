package px.seisen.stages;

public abstract class BaseStage {
    private final double startTimestamp;
    private String stageName, stageId;
    private int stageHeight;

    public BaseStage(String stageName, String stageId, int stageHeight) {
        this.startTimestamp = System.currentTimeMillis();
        this.stageName = stageName;
        this.stageId = stageId;
        this.stageHeight = 80;
    }

    public double getElapsedTime() {
        return System.currentTimeMillis() - this.startTimestamp;
    }

    public int getStageHeight() {
        return stageHeight;
    }

    public String getStageId() {
        return stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageHeight(int stageHeight) {
        this.stageHeight = stageHeight;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }
}
