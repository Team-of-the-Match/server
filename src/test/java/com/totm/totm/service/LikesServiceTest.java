package com.totm.totm.service;

import com.totm.totm.dto.PostDto;
import com.totm.totm.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.totm.totm.dto.LikesDto.LikeRequestDto;

@SpringBootTest
@Rollback(value = false)
class LikesServiceTest {

    @Autowired
    LikesService likesService;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private final int numOfThreads = 1000;

    @Test
    public void 비관적락_성능테스트() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        for(long i = 1; i <= numOfThreads; i++) {
            long finalI = i;
            executorService.execute(() -> {
                try {
                    LikeRequestDto request = new LikeRequestDto();
                    request.setMemberId(finalI);
                    request.setPostId(1L);
                    likesService.like(request);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();
        executorService.shutdown();

        List<PostDto.PostsResponseDto> posts = postService.findPosts(2L);
        Assertions.assertThat(posts.get(0).getLikeNum()).isEqualTo(numOfThreads);
    }

    @Test
    public void 낙관적락_성능테스트() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        for(long i = 1; i <= numOfThreads; i++) {
            long finalI = i;
            executorService.execute(() -> {
                try {
                    LikeRequestDto request = new LikeRequestDto();
                    request.setMemberId(finalI);
                    request.setPostId(1L);

                    while(true) {
                        try {
                            likesService.likeWithOptimisticLock(request);
                            break;
                        } catch(Exception e) {
//                            Thread.sleep(10);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();
        executorService.shutdown();

        List<PostDto.PostsResponseDto> posts = postService.findPosts(2L);
        Assertions.assertThat(posts.get(0).getLikeNum()).isEqualTo(numOfThreads);
    }

    @Test
    public void 레디스_성능테스트() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(numOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        for(long i = 1; i <= numOfThreads; i++) {
            long finalI = i;
            executorService.execute(() -> {
                try {
                    LikeRequestDto request = new LikeRequestDto();
                    request.setMemberId(finalI);
                    request.setPostId(1L);

                    likesService.likeWithRedis(request);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });

        }
        latch.await();
        executorService.shutdown();

        Integer likes = (Integer) redisTemplate.opsForValue().get("1");
        Assertions.assertThat(likes).isEqualTo(numOfThreads);
    }

}