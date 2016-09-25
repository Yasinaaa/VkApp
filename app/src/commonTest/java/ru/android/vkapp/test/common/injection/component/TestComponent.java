package ru.android.vkapp.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.android.vkapp.injection.component.ApplicationComponent;
import ru.android.vkapp.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
