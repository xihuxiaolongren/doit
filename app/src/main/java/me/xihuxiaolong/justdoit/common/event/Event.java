package me.xihuxiaolong.justdoit.common.event;

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
}
