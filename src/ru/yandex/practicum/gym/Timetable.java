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

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay == null) {
            return null;
        }
        return sessionsForDay.get(timeOfDay);
    }


}
