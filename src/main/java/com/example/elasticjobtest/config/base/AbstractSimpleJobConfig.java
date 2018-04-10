package com.example.elasticjobtest.config.base;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resource;

/**
 * create by adolph on 2018/4/9
 */

public abstract class AbstractSimpleJobConfig
{
    @Resource
    protected ZookeeperRegistryCenter regCenter;

    @Resource
    protected JobEventConfiguration jobEventConfiguration;


    protected LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                        final String cron,
                                                        final int shardingTotalCount,
                                                        final String shardingItemParameters,
                                                        final String description)
    {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(
                jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).description(description).build(), jobClass.getCanonicalName())).overwrite(true).build();
    }

    public JobScheduler builderJobScheduler(final SimpleJob simpleJob,
                                            final String cron,
                                            final int shardingTotalCount,
                                            final String shardingItemParameters,
                                            final String description)
    {
        return new SpringJobScheduler(simpleJob,
                regCenter,
                getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters, description),
                jobEventConfiguration);
    }

    public abstract SimpleJob simpleJob();

    public abstract JobScheduler simpleJobScheduler(final SimpleJob simpleJob,
                                           final String cron,
                                           final int shardingTotalCount,
                                           final String shardingItemParameters,
                                           final String description);
}
