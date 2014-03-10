package com.thunisoft.mediax.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * unsafe
 * 
 * @param <V>
 * @since V1.0 2014-3-6
 * @author chenxh
 */
public class SimpleFutureTask<V> implements Future<V> {
    private Semaphore semaphore = new Semaphore(0);

    private boolean isDone = false;
    private boolean isCancelled = false;
    private V value = null;
    private Throwable cause;

    public SimpleFutureTask() {
        this(null);
    }
    public SimpleFutureTask(V defaultValue) {
        this.value = defaultValue;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        setDone(null, mayInterruptIfRunning, null);

        return true;
    }


    public void put(V value) {
        setDone(value, false, null);
    }

    public void fail(Throwable cause) {
        setDone(null, false, cause);
    }

    private void setDone(V value, boolean isCancelled, Throwable cause) {
        if (!isDone) {
            this.value = value;
            this.isCancelled = isCancelled;
            this.cause = cause;
            this.isDone = true;

            realseTransferLock();
        }

    }


    private void realseTransferLock() {
        Semaphore s = semaphore;
        if (null != s) {
            s.release(Short.MAX_VALUE);
        }

        semaphore = null;
    }

    public Throwable cause() {
        return cause;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        // 已经终止
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        Semaphore s = semaphore;
        if (null != s) {
            s.acquire();
        }

        return executResult();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException,
            ExecutionException {
        // 已经终止
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        Semaphore s = semaphore;
        if (null != s) {
            s.tryAcquire(timeout, unit);
        }

        return executResult();
    }


    private V executResult() throws ExecutionException {
        if (null != cause) {
            throw new ExecutionException(cause);
        } else {
            return value;
        }
    }
}
