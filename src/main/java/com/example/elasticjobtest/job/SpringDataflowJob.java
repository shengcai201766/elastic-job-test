/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.example.elasticjobtest.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpringDataflowJob implements DataflowJob<String>
{
    private volatile int page = 0, size = 100;
    
    @Override
    public List<String> fetchData(final ShardingContext shardingContext)
    {
        // TODO 取数据方法-当返回数据为null或者数据长度为0的实时，JOB完成本次任务
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW FETCH"));

        List<String> data = new ArrayList<>();

        if(page++ == 0)
        {
            data.add("data 1。");
            data.add("data 2。");
            data.add("data 3。");
        }
        else if(page > 0)
        {
            data.add("data 4。");
            data.add("data 5。");
            data.add("data 6。");

            page = -1;  // 结束下一次数据返回
        }
        else
        {
            page = 0;   // 重置页码
        }


        return data;
    }
    
    @Override
    public void processData(final ShardingContext shardingContext, final List<String> data) {
        System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
                shardingContext.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "DATAFLOW PROCESS"));

        // TODO 数据处理
        for (String each : data) {
            System.out.println(shardingContext.getShardingItem() + " ==> " + each);
        }
    }
}
