package com.notice_board.common.config;

import com.notice_board.common.component.SystemValues;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <pre>
 * Spring Async Config
 * </pre>
 * <pre>
 * <b>History:</b>
 * 		Park Jun Mo, 1.0, 2024-11-30 초기작성
 * </pre>
 *
 * @author Park Jun Mo
 * @version 1.0
 * @since 1.0
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20); // 기본 스레드 수
		executor.setMaxPoolSize(100); // 최대 스레드 수
		executor.setQueueCapacity(500); // Queue 사이즈
		executor.setThreadNamePrefix(SystemValues.PREFIX_THREAD_NAME.getValue());
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 예외와 누락 없이 최대한 처리
		executor.setWaitForTasksToCompleteOnShutdown(true); // queue 남아 있는 모든 작업이 완료될 때까지 기다림
		executor.setAwaitTerminationSeconds(60); // 최대 대기 시간
		executor.initialize();
		return executor;
	}
}
