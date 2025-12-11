package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private HashMap<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> sessionsForDay = timetable.get(day);
        if (sessionsForDay == null) {
            sessionsForDay = new TreeMap<>();
            timetable.put(day, sessionsForDay);
        }
        ArrayList<TrainingSession> sessionsForTime = sessionsForDay.get(time);
        if (sessionsForTime == null) {
            sessionsForTime = new ArrayList<>();
            sessionsForDay.put(time, sessionsForTime);
        }
        sessionsForTime.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay == null) {
            return new ArrayList<>();
        }
        ArrayList<TrainingSession> result = new ArrayList<>();
        for(ArrayList<TrainingSession> trainingSessions: sessionsForDay.values()){
            result.addAll(trainingSessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay == null) {
            return new ArrayList<>();
        }
        return sessionsForDay.get(timeOfDay);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> coachCounter = new HashMap<>();

        for (TreeMap<TimeOfDay, ArrayList<TrainingSession>> sessionForDay : timetable.values()) {
            for(ArrayList<TrainingSession> sessionsAtTime: sessionForDay.values()){
                for(TrainingSession trainingSession: sessionsAtTime){
                    Coach coach = trainingSession.getCoach();
                    coachCounter.put(coach, coachCounter.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> countOfTrainings = new ArrayList<>();
        for(Map.Entry<Coach, Integer> entry: coachCounter.entrySet()){
            countOfTrainings.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        countOfTrainings.sort((c1, c2) -> c2.getCount().compareTo(c1.getCount()));

        return countOfTrainings;

    }

    public static class CounterOfTrainings {
        private final Coach coach;
        private final Integer count;

        public CounterOfTrainings(Coach coach, Integer count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public Integer getCount() {
            return count;
        }

        @Override
        public String toString() {
            return "Количество тренировок у каждого тренера: " +
                    "тренер - " + coach +
                    ", количество тренировок - " + count;
        }
    }
}
