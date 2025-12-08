package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> mondaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Assertions.assertNotNull(mondaySessions);
        Assertions.assertEquals(1, mondaySessions.size());

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> tuesdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertNull(tuesdaySessions);
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> mondaySession =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertNotNull(mondaySession);
        Assertions.assertEquals(1, mondaySession.size());

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> thursdaySession =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertNotNull(thursdaySession);
        Assertions.assertEquals(2, thursdaySession.size());

        List<TimeOfDay> thursdayTimes = new ArrayList<>(thursdaySession.keySet());
        Assertions.assertEquals(new TimeOfDay(13, 0), thursdayTimes.get(0));
        Assertions.assertEquals(new TimeOfDay(20, 0), thursdayTimes.get(1));

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> tuesdaySessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertNull(tuesdaySessions);
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondayAt13 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertNotNull(mondayAt13);
        Assertions.assertEquals(new TimeOfDay(13, 0), mondayAt13.get(0));
        List<TrainingSession> mondayAt14 =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertNull(mondayAt14);
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Андрей", "Петрович");

        Group group1 = new Group("Танцы для начинающих", Age.ADULT, 60);
        Group group2 = new Group("Танцы для продвинутых", Age.ADULT, 90);

        TrainingSession trainingSession1 =
                new TrainingSession(group1, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession trainingSession2 =
                new TrainingSession(group2, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        List<TrainingSession> wednesdayAt18 = new ArrayList<>(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));

        Assertions.assertNotNull(wednesdayAt18);
        Assertions.assertEquals(2, wednesdayAt18.size());

        Assertions.assertTrue(wednesdayAt18.contains(trainingSession1));
        Assertions.assertTrue(wednesdayAt18.contains(trainingSession2));
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        for (DayOfWeek day : DayOfWeek.values()) {
            Assertions.assertNull(timetable.getTrainingSessionsForDay(day));

            for (int i = 0; i < 23; i++) {
                Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(day, new TimeOfDay(i, 0)));
            }
        }
    }

    @Test
    void testSessionOrderingInSameTimeSlot() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Сидоров", "Алексей", "Владимирович");

        Group group1 = new Group("Пилатес", Age.ADULT, 60);
        Group group2 = new Group("Стретчинг", Age.ADULT, 45);

        TrainingSession session2 = new TrainingSession(
                group2, coach, DayOfWeek.FRIDAY, new TimeOfDay(19, 0));
        TrainingSession session1 = new TrainingSession(
                group1, coach, DayOfWeek.FRIDAY, new TimeOfDay(19, 0));

        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session1);

        List<TrainingSession> fridayAt19 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.FRIDAY, new TimeOfDay(19, 0));
        Assertions.assertNotNull(fridayAt19);
        Assertions.assertEquals(2, fridayAt19.size());
        Assertions.assertEquals(session2, fridayAt19.get(0));
        Assertions.assertEquals(session1, fridayAt19.get(1));
    }

    @Test
    void testDifferentDaysDifferentSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Петров", "Дмитрий", "Алексеевич");

        Group group = new Group("Кроссфит", Age.ADULT, 120);


        TrainingSession mondaySession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        TrainingSession wednesdaySession = new TrainingSession(
                group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0));
        TrainingSession fridaySession = new TrainingSession(
                group, coach, DayOfWeek.FRIDAY, new TimeOfDay(9, 0));

        timetable.addNewTrainingSession(mondaySession);
        timetable.addNewTrainingSession(wednesdaySession);
        timetable.addNewTrainingSession(fridaySession);


        Assertions.assertNotNull(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY));
        Assertions.assertNotNull(timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY));
        Assertions.assertNotNull(timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY));


        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY));
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.SATURDAY));
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY));


        List<TrainingSession> mondayAt9 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(9, 0));
        Assertions.assertNotNull(mondayAt9);
        Assertions.assertEquals(1, mondayAt9.size());
        Assertions.assertEquals(mondaySession, mondayAt9.get(0));
    }
}
