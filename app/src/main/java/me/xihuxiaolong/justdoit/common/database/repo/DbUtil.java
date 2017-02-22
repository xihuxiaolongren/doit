package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDODao;

public class DbUtil {
    private static CacheRepo cacheRepo;
    private static RedoPlanRepo redoPlanRepo;
    private static PlanRepo planRepo;
    private static PlanHistoryRepo planHistoryRepo;
    private static TargetRepo targetRepo;
    private static TagRepo tagRepo;
    private static BacklogRepo backlogRepo;

    public static CacheRepo getCacheRepo() {
        if (cacheRepo == null) {
            cacheRepo = new CacheRepo(DbCore.getDaoSession().getCacheDODao());
        }
        return cacheRepo;
    }

    public static RedoPlanRepo getRedoPlanRepo() {
        if (redoPlanRepo == null) {
            redoPlanRepo = new RedoPlanRepo(DbCore.getDaoSession().getRedoPlanDODao());
        }
        return redoPlanRepo;
    }

    public static PlanRepo getPlanRepo() {
        if (planRepo == null) {
            planRepo = new PlanRepo(DbCore.getDaoSession().getPlanDODao());
        }
        return planRepo;
    }

    public static PlanHistoryRepo getPlanHistoryRepo() {
        if (planHistoryRepo == null) {
            planHistoryRepo = new PlanHistoryRepo(DbCore.getDaoSession().getPlanHistoryDODao());
        }
        return planHistoryRepo;
    }

    public static TargetRepo getTargetRepo() {
        if (targetRepo == null) {
            targetRepo = new TargetRepo(DbCore.getDaoSession().getTargetDODao());
        }
        return targetRepo;
    }

    public static TagRepo getTagRepo() {
        if (tagRepo == null) {
            tagRepo = new TagRepo(DbCore.getDaoSession().getTagDODao());
        }
        return tagRepo;
    }

    public static BacklogRepo getBacklogRepo() {
        if (backlogRepo == null) {
            backlogRepo = new BacklogRepo(DbCore.getDaoSession().getBacklogDODao());
        }
        return backlogRepo;
    }

}