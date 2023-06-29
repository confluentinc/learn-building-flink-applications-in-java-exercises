package userstatistics;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import models.UserStatistics;

class ProcessUserStatisticsFunction extends ProcessWindowFunction<UserStatistics, UserStatistics, String, TimeWindow> {
    private ValueStateDescriptor<UserStatistics> stateDescriptor;

    @Override
    public void open(Configuration parameters) throws Exception {
        stateDescriptor = new ValueStateDescriptor<>("User Statistics", UserStatistics.class);
        super.open(parameters);
    }

    @Override
    public void process(String emailAddress, ProcessWindowFunction<UserStatistics, UserStatistics, String, TimeWindow>.Context context, Iterable<UserStatistics> statsList, Collector<UserStatistics> collector) throws Exception {
        ValueState<UserStatistics> state = context.globalState().getState(stateDescriptor);
        UserStatistics accumulatedStats = state.value();

        for (UserStatistics newStats: statsList) {
            if(accumulatedStats == null)
                accumulatedStats = newStats;
            else
                accumulatedStats = accumulatedStats.merge(newStats);
        }

        state.update(accumulatedStats);

        collector.collect(accumulatedStats);
    }
}