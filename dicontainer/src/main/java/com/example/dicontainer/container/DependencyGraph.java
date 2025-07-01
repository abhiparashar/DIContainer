package com.example.dicontainer.container;

import java.lang.reflect.Constructor;
import java.util.*;

public class DependencyGraph {
    private final Map<Class<?>, Set<Class<?>>> dependencies = new HashMap<>();
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    public void addBinding(Class<?> interfaceClass, Class<?> implementationClass) {
        bindings.put(interfaceClass, implementationClass);
        analyzeDependencies(implementationClass);
    }

    private void analyzeDependencies(Class<?> clazz) {
        Set<Class<?>> classDependencies = new HashSet<>();

        // Find constructor with most parameters (our strategy)
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = Arrays.stream(constructors)
                    .max(Comparator.comparing(Constructor::getParameterCount))
                    .orElse(constructors[0]);

            // Record dependencies
            for (Class<?> paramType : constructor.getParameterTypes()) {
                classDependencies.add(paramType);
            }
        }

        dependencies.put(clazz, classDependencies);
    }

    public List<Class<?>> getCreationOrder() {
        List<Class<?>> result = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        Set<Class<?>> visiting = new HashSet<>();

        for (Class<?> clazz : dependencies.keySet()) {
            if (!visited.contains(clazz)) {
                topologicalSort(clazz, visited, visiting, result);
            }
        }

        return result;
    }

    private void topologicalSort(Class<?> clazz, Set<Class<?>> visited,
                                 Set<Class<?>> visiting, List<Class<?>> result) {
        if (visiting.contains(clazz)) {
            throw new RuntimeException("Circular dependency detected involving: " + clazz.getName());
        }

        if (visited.contains(clazz)) {
            return;
        }

        visiting.add(clazz);

        // Visit dependencies first
        Set<Class<?>> deps = dependencies.get(clazz);
        if (deps != null) {
            for (Class<?> dep : deps) {
                Class<?> actualDep = bindings.getOrDefault(dep, dep);
                if (dependencies.containsKey(actualDep)) {
                    topologicalSort(actualDep, visited, visiting, result);
                }
            }
        }

        visiting.remove(clazz);
        visited.add(clazz);
        result.add(clazz);
    }

    public void printDependencyGraph() {
        System.out.println("\n=== DEPENDENCY GRAPH ===");
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : dependencies.entrySet()) {
            System.out.println(entry.getKey().getSimpleName() + " depends on: " +
                    entry.getValue().stream()
                            .map(Class::getSimpleName)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("nothing"));
        }

        System.out.println("\n=== CREATION ORDER ===");
        List<Class<?>> order = getCreationOrder();
        for (int i = 0; i < order.size(); i++) {
            System.out.println((i + 1) + ". " + order.get(i).getSimpleName());
        }
        System.out.println();
    }
}
