import java.lang.reflect.Method;

public class YSingleton {
    private final static String methodName = "doSomething";
    public static void checkEnumSigleton(String className){
        try {
            Class<?> demo = Class.forName(className);
            EnumSingleton instance[] = (EnumSingleton[]) demo.getEnumConstants();
            Method method = demo.getMethod(methodName);
            System.out.println("反射调用" + className + "结果");
            method.invoke(instance[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("常规调用" + className + "结果");
        EnumSingleton.INSTANCE.doSomething();
    }
    public static void checkSingleton(String className){
        try {
            Class<?> demo = Class.forName(className);
            Method method = demo.getMethod(methodName);
            Object obj = demo.newInstance();
            System.out.println("反射调用" + className + "结果");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("常规调用" + className + "结果");
        LazySingleton.getInstance().doSomething();
    }
    public static void checkThreadSingleton(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "调用结果");
                LazySingleton.getInstance().doSomething();
            }
        };
        Thread t1 = new Thread(run, "线程1");
        Thread t2 = new Thread(run, "线程2");
        t1.start();
        t2.start();
    }
    public static void main(String args[]){
        checkEnumSigleton("EnumSingleton");
        checkSingleton("LazySingleton");
        checkThreadSingleton();
    }
}
/**
 * 枚举单例,单元素的枚举类型是实现单例的最好方式
 * 1.没有线程同步问题
 * 2.自己处理序列化
 * 3.防止反射攻击
 * @author yanzihan
 *
 */
enum EnumSingleton{
    INSTANCE;
    public void doSomething(){
        System.out.println(this.hashCode());
    }
    EnumSingleton(){}
}

abstract class Singleton{
    public abstract void doSomething();
}
/**
 * 饿汉模式
 * 1.没有Lazy Loading
 * 2.线程安全
 * @author yanzihan
 *
 */
class HungrySingleton extends Singleton{
    private static HungrySingleton instance = new HungrySingleton();
    
    public static HungrySingleton getInstance(){
        return instance;
    }
    public void doSomething(){
        System.out.println(this.hashCode());
    }
}
/**
 * 懒汉模式
 * 1.Lazy Loading
 * 2.线程不安全
 * @author yanzihan
 *
 */
class LazySingleton{
    private static LazySingleton instance;
    
    public static LazySingleton getInstance(){
        if(instance == null){
            instance = new LazySingleton();
        }
        return instance;
    }
    
    public void doSomething(){
        System.out.println(this.hashCode());
    }
}
/**
 * 静态内部类
 * 1.静态内部类只会加载一次,避免了线程问题
 * 2.Lazy Loading
 * @author yanzihan
 *
 */
class InternalSingleton{
    private static class SingletonHolder{
        private final static  InternalSingleton INSTANCE=new InternalSingleton();
    }
    
    public static InternalSingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public void doSomething(){
        System.out.println(this.hashCode());
    }
}
/**
 * 双重校验锁
 * @author yanzihan
 *
 */
class LockSingleton{
    private volatile static LockSingleton instance;
    private LockSingleton(){}
      
    public static LockSingleton getInstance(){
        if(instance == null){
            synchronized(LockSingleton.class){
                if(instance == null){
                    instance = new LockSingleton();
                }
            }
        }
        return instance;
    }
      
}