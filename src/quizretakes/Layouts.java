package quizretakes;

public enum Layouts {
    APPOINTMENTS {
        @Override
        public String toString() {
            return "/layouts/appointments.fxml";
        }
    },

    LOGIN {
        @Override
        public String toString() {
            return "/layouts/login.fxml";
        }
    },

    SCHEDULE {
        @Override
        public String toString() {
            return "/layouts/schedule.fxml";
        }
    }
}
