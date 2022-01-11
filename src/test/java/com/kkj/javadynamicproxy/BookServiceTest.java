package com.kkj.javadynamicproxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class BookServiceTest {


    BookService bookService = (BookService) Proxy.newProxyInstance(BookService.class.getClassLoader()
            , new Class[]{BookService.class}, new InvocationHandler() {

                BookService bookService = new DefaultBookService();

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("rent")) {

                        System.out.println("aaaaa");
                        Object invoke = method.invoke(bookService, args);
                        System.out.println("bbbbb");
                        return invoke;
                    }
                    return method.invoke(bookService, args);
                }
            });

    @Test
    public void di() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        /*
        cglib를 이용한 클래스에 프록시 사용 예제
        MethodInterceptor handler = new MethodInterceptor() {
            BookService bookService = new DefaultBookService();
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("aaaa");
                Object invoke = method.invoke(bookService, objects);
                System.out.println("bbbb");
                return invoke;
            }
        };
        Enhancer.create(BookService.class, handler);
        */

        /* Bytebuddy를 사용한 클래스 프록시 예제 */
        Class<? extends DefaultBookService> proxyClass = new ByteBuddy().subclass(DefaultBookService.class)
                .method(named("rent")).intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                    DefaultBookService bookService = new DefaultBookService();

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("aaaa");
                        Object invoke = method.invoke(bookService, args);
                        System.out.println("bbbb");
                        return invoke;

                    }
                }))
                .make().load(DefaultBookService.class.getClassLoader()).getLoaded();
        DefaultBookService defaultBookService = proxyClass.getConstructor(null).newInstance();
        /* End */
        Book book = new Book();
        book.setTitle("spring");
        defaultBookService.rent(book);
        defaultBookService.returnBook(book);

    }
}
