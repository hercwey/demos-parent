package com.design.pattern.observer;

import com.design.pattern.observer.ConcreteObserver;
import com.design.pattern.observer.ConcreteSubject;

/**
*@Describetion
*@author  Devonmusa
*@date 2017年2月28日
*/
public class ObserverPatternTest {

	public static void main(String[] args) {
		
		ConcreteSubject concreteSubject = new ConcreteSubject();
		
		ConcreteObserver observerA = new ConcreteObserver();
		ConcreteObserver observerC = new ConcreteObserver();
		ConcreteObserver observerD = new ConcreteObserver();
		concreteSubject.attach(observerA);
		concreteSubject.attach(observerC);
		concreteSubject.attach(observerD);
		
		concreteSubject.setSujectState("今天天气特别好");
		
		concreteSubject.setSujectState("今天是阴天");

	}

}
