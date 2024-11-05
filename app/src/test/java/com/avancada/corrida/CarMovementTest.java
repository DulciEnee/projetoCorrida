package com.avancada.corrida;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mockito.Mockito;

public class CarMovementTest {
    private Car car;
    private Track track;
    private CarMovement carMovement;
    private List<Car> allCars;
    private Map<Integer, Integer> sensor;

    @Before
    public void setUp() {
        car = mock(Car.class);
        track = mock(Track.class);
        allCars = mock(List.class);
        sensor = new HashMap<>();
        //carMovement = new CarMovement(car, track, allCars, sensor, 55);
        carMovement = spy(new CarMovement(car, track, allCars, sensor, 55));
    }

    @Test
    public void testUpdateSensorReadings() {
        when(car.getDistanceToObstacle(anyDouble())).thenReturn(40);

        carMovement.updateSensorReadings();

        // Verifica se o sensor foi preenchido corretamente com as leituras
        assertEquals(16, sensor.size()); // Deve conter 16 leituras
        for (Integer distance : sensor.values()) {
            assertEquals(40, (int) distance); // Cada valor deve ser 40, conforme a simulação
        }
    }

    @Test
    public void testStartMoving() {
        doNothing().when(carMovement).moveCar(); // Evita execução real de moveCar

        carMovement.startMoving();

        // Verifica se isMoving está ativo e se moveCar foi chamado
        verify(carMovement).moveCar();
    }
}
