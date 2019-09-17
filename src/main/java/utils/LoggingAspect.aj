package utils;

public aspect LoggingAspect {
    pointcut log(): execution(@Log public * *(..));

    before(): log() {
        System.out.println(thisJoinPoint.getTarget() + "#" + thisJoinPoint.getSignature() + " has been invoked");
    }
}