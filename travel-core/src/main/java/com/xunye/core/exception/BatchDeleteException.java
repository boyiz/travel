package com.xunye.core.exception;


import lombok.Data;

@Data
public class BatchDeleteException extends BusinessException {

    public BatchDeleteException() {
    }

    public BatchDeleteException(String msg) {
        super(msg);
    }
}
