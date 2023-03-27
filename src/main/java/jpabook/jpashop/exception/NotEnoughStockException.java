package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException{
    // 오버라이딩 메소드를 해줘야 하는 이유 => 메세지 같은걸 넘겨주고, 메세지 + 예외가 발생한 근원적인 이셉션을 넣어서 이셉션 트레이스를 쭉 나오게 할 수 있다.

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
