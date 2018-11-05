package edu.java8.practice.service.impl;

import edu.java8.practice.domain.Privilege;
import edu.java8.practice.domain.User;
import edu.java8.practice.service.UserService;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class UserServiceImpl implements UserService {

    @Override
    public List<User> sortByAgeDescAndNameAsc(final List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .sorted(comparing(User::getAge)
                        .reversed()
                        .thenComparing(User::getFirstName))
                .collect(toList());
    }

    @Override
    public List<Privilege> getAllDistinctPrivileges(final List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .map(User::getPrivileges)
                .flatMap(List::stream)
                .distinct()
                .collect(toList());
    }

    @Override
    public Optional<User> getUpdateUserWithAgeHigherThan(final List<User> users, final int age) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .filter(u -> u.getAge() > age)
                .filter(u -> u.getPrivileges().contains(Privilege.UPDATE))
                .findAny();
    }

    @Override
    public Map<Integer, List<User>> groupByCountOfPrivileges(final List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .collect(groupingBy(u -> u.getPrivileges().size()));
    }

    @Override
    public double getAverageAgeForUsers(final List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .mapToInt(User::getAge)
                .average()
                .orElse(-1);
    }

    @Override
    public Optional<String> getMostFrequentLastName(final List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .collect(groupingBy(User::getLastName, counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .filter(e -> !users.stream()
                        .collect(groupingBy(User::getLastName, counting()))
                        .entrySet().removeIf(t -> t.getValue() == e.getValue() && t.getKey() != e.getKey()))
                .map(Map.Entry::getKey);
    }

    @Override
    public List<User> filterBy(final List<User> users, final Predicate<User>... predicates) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .filter(Arrays.stream(predicates).reduce(Predicate::and).orElse(p -> true))
                .collect(toList());
    }

    @Override
    public String convertTo(final List<User> users, final String delimiter, final Function<User, String> mapFun) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .map(t -> mapFun.apply(t))
                .collect(joining(delimiter));
    }

    @Override
    public Map<Privilege, List<User>> groupByPrivileges(List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .map(u -> u.getPrivileges().stream()
                        .map(p -> new Pair<>(p, u))
                        .collect(toSet()))
                .flatMap(Collection::stream)
                .collect(groupingBy(Pair::getKey, mapping(Pair::getValue, toList())));
    }

    @Override
    public Map<String, Long> getNumberOfLastNames(final List<User> users) {
        //throw new UnsupportedOperationException("Not implemented");
        return users.stream()
                .map(User::getLastName)
                .collect(groupingBy(identity(), counting()));
    }
}
