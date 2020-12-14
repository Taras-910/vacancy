package ua.training.top.to;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DoubleWordTo implements Serializable {

    @NotNull
    private String languageTask;
    @NotNull
    private String workplaceTask;

    public DoubleWordTo(String languageTask, String workplaceTask) {
        this.languageTask = languageTask;
        this.workplaceTask = workplaceTask;
    }

    public DoubleWordTo(){}

    public String getLanguageTask() {
        return languageTask;
    }

    public void setLanguageTask(String languageTask) {
        this.languageTask = languageTask;
    }

    public String getWorkplaceTask() {
        return workplaceTask;
    }

    public void setWorkplaceTask(String workplaceTask) {
        this.workplaceTask = workplaceTask;
    }

    @Override
    public String toString() {
        return "DoubleWordTo{" +
                "\nlanguageTask='" + languageTask + '\'' +
                ", \nworkplaceTask='" + workplaceTask + '\'' +
                '}';
    }
}
