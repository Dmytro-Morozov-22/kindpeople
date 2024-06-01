package com.dmtrmrzv.kindpeople;

import com.dmtrmrzv.kindpeople.utils.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;

@SpringBootApplication
public class KindPeopleApplication {
	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(KindPeopleApplication.class, args);

//		ShowBeansInSpringContext ();
	}

	public static void  ShowBeansInSpringContext () {
		ApplicationContext context = ApplicationContextProvider.getApplicationContext();
		String[] beanNames = context.getBeanDefinitionNames();
		System.out.println("\nKindPeople bean names in Spring Context:");
		ArrayList<String> myBeans = new ArrayList<>();
		int counterAll = 0;
		for (String beanName : beanNames) {
			counterAll++;
			if(!beanName.startsWith("org") && !beanName.startsWith("jpa") && !beanName.startsWith("spring")) {
				myBeans.add(beanName);
							}
		}
		myBeans.forEach(System.out::println);
		System.out.println("Number of all beans = " + counterAll );
		System.out.println("Number of beans without 'org' and 'jpa' and 'spring' = " + myBeans.size());
	}

}
