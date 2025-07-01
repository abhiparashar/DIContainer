package com.example.dicontainer.container;

import java.lang.reflect.Constructor;
import java.util.*;

public class SimpleDIContainer {
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();
    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final Set<Class<?>> resolutionStack = new HashSet<>();
    private final DependencyGraph dependencyGraph = new DependencyGraph();

    public <T> void bind(Class<T> interfaceClass, Class<? extends T> implementationClass) {
        bindings.put(interfaceClass, implementationClass);
        dependencyGraph.addBinding(interfaceClass, implementationClass);
    }

    public <T> void singleton(Class<T> clazz, T instance) {
        singletons.put(clazz, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        if (singletons.containsKey(clazz)) {
            return (T) singletons.get(clazz);
        }

        if (resolutionStack.contains(clazz)) {
            System.out.println("clazz");
            throw new RuntimeException("Circular dependency detected: " + clazz.getName());
        }

        resolutionStack.add(clazz);
        try {
            Class<?> implementationClass = bindings.getOrDefault(clazz, clazz);
            T instance = createInstance((Class<T>) implementationClass);
            singletons.put(clazz, instance);
            return instance;
        } finally {
            resolutionStack.remove(clazz);
        }
    }

    private <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<?> constructor = Arrays.stream(constructors)
                    .max(Comparator.comparing(Constructor::getParameterCount))
                    .orElseThrow(() -> new RuntimeException("No constructor found"));

            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = get(paramTypes[i]);
            }

            return (T) constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }

    public void printDependencyGraph() {
        dependencyGraph.printDependencyGraph();
    }

    public void initializeAll() {
        System.out.println("=== INITIALIZING ALL SERVICES ===");
        List<Class<?>> creationOrder = dependencyGraph.getCreationOrder();
        for (Class<?> clazz : creationOrder) {
            if (!singletons.containsKey(clazz)) {
                get(clazz);
            }
        }
        System.out.println("=== INITIALIZATION COMPLETE ===\n");
    }
}