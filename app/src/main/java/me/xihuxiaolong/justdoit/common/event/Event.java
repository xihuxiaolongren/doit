package me.xihuxiaolong.justdoit.common.event;

import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class Event {

    public static class DeletePlan {

        public PlanDO plan;

        public DeletePlan(PlanDO plan) {
            this.plan = plan;
        }
    }

    public static class AddPlan {

        public PlanDO plan;

        public AddPlan(PlanDO plan) {
            this.plan = plan;
        }
    }

    public static class UpdatePlan {

        public PlanDO plan;

        public UpdatePlan(PlanDO plan) {
            this.plan = plan;
        }
    }

    public static class DeleteBacklog {

        public BacklogDO backlog;

        public DeleteBacklog(BacklogDO backlog) {
            this.backlog = backlog;
        }
    }

    public static class AddBacklog {

        public BacklogDO backlog;

        public AddBacklog(BacklogDO backlog) {
            this.backlog = backlog;
        }
    }

    public static class UpdateBacklog {

        public BacklogDO backlog;

        public UpdateBacklog(BacklogDO backlog) {
            this.backlog = backlog;
        }
    }

    public static class DeleteTarget {

        public String targetName;

        public DeleteTarget(String targetName) {
            this.targetName = targetName;
        }
    }

    public static class AddTarget {

        public String targetName;

        public AddTarget(String targetName) {
            this.targetName = targetName;
        }
    }

    public static class UpdateTarget {

        public String targetName;

        public UpdateTarget(String targetName) {
            this.targetName = targetName;
        }
    }

    public static class UpdateSettings {

        public UserSettings userSettings;

        public UpdateSettings(UserSettings userSettings) {
            this.userSettings = userSettings;
        }
    }

    public static class ChangeDayNightTheme {

        public ChangeDayNightTheme() {
        }
    }
}
