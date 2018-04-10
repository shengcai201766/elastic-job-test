package com.example.elasticjobtest.config.base;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import javax.annotation.Resource;

/**
 * create by adolph on 2018/4/9
 */

public abstract class AbstractDataflowJobConfig
{
    @Resource
    protected ZookeeperRegistryCenter regCenter;

    @Resource
    protected JobEventConfiguration jobEventConfiguration;

    protected LiteJobConfiguration getLiteJobConfiguration(final Class<? extends DataflowJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(JobCoreConfiguration.newBuilder(
                jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(), jobClass.getCanonicalName(), true)).overwrite(true).build();
    }

    public JobScheduler builderJobScheduler(final DataflowJob dataflowJob,
                                            final String cron,
                                            final int shardingTotalCount,
                                            final String shardingItemParameters)
    {
        return new SpringJobScheduler(dataflowJob,
                regCenter,
                getLiteJobConfiguration(dataflowJob.getClass(), cron, shardingTotalCount, shardingItemParameters),
                jobEventConfiguration);
    }

    public abstract DataflowJob dataflowJob();

    public abstract JobScheduler dataflowJobScheduler(final DataflowJob dataflowJob, final String cron, final int shardingTotalCount, final String shardingItemParameters);
}
