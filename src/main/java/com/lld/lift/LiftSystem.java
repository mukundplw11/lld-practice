package com.lld.lift;

import java.util.List;


/**
 *
 * Lift movement - Up/Down & Stop at specific floors
 * Users - Request for lift for up/down and select floor in lift
 * Lift - Handles user pickup logic, display current floor &
 */
public class LiftSystem {


    public static void main(String[] args) {

    }

    enum LiftStatus {
        UP,
        DOWN,
        IDLE
    }

    class ElevatorController {
        private Elevator elevator;

        private Integer minFloor;

        private Integer maxFloor;
    }

    class ElevatorSystem {

        private Integer totalFloors;

        private List<ElevatorController> elevatorControllers;

        public Integer requestElevator(Integer currentFloor, Integer destinationFloor) {
            return null;
        }

    }

    class Elevator {
        private Integer id;

        private Integer currentFloor;

        private Integer nextStopFloor;

        private LiftStatus status;

        private Boolean doorOpen;
    }

    class Panel {

    }

    class ExternalPanel extends Panel {

    }

    class InternalPanel extends Panel {

    }
}

