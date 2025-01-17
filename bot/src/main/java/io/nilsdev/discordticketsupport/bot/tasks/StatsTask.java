/*
 * MIT License
 *
 * Copyright (c) 2023 nils
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.nilsdev.discordticketsupport.bot.tasks;

import com.google.inject.Inject;
import io.nilsdev.discordticketsupport.common.models.StatsModel;
import io.nilsdev.discordticketsupport.common.repositories.StatsRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StatsTask implements Runnable {

    private final ShardManager shardManager;
    private final StatsRepository statsRepository;

    @Inject
    public StatsTask(ShardManager shardManager, StatsRepository statsRepository) {
        this.shardManager = shardManager;
        this.statsRepository = statsRepository;
    }

    @Override
    public void run() {
        List<StatsModel> historicalStats = statsRepository.getAllStats();

        AtomicInteger totalGuilds = new AtomicInteger();
        AtomicInteger totalMembers = new AtomicInteger();
        AtomicLong totalMessagesSent = new AtomicLong();

        Map<String, Integer> memberCountByStatus = new HashMap<>();
        Map<String, Integer> channelCountByType = new HashMap<>();

        this.shardManager.getShards().forEach(jda -> {
            for (Guild guild : jda.getGuilds()) {
                totalGuilds.incrementAndGet();
                totalMembers.addAndGet(guild.getMemberCount());

                collectMemberCountsByStatus(memberCountByStatus, guild);

                collectChannelCountsByType(channelCountByType, guild);
            }
        });

        StatsModel currentStats = StatsModel.builder()
                .guilds(totalGuilds.get())
                .members(totalMembers.get())
                .messagesSent(totalMessagesSent.get())
                .memberCountByStatus(memberCountByStatus)
                .channelCountByType(channelCountByType)
                .build();

        statsRepository.save(currentStats);

        performComplexAnalysis(historicalStats);
    }

    private void collectMemberCountsByStatus(Map<String, Integer> memberCountByStatus, Guild guild) {
        
    }

    private void collectChannelCountsByType(Map<String, Integer> channelCountByType, Guild guild) {
        
    }

    private void performComplexAnalysis(List<StatsModel> historicalStats) {
       
    }
}
