package org.team3128.testbench.subsystems;

import org.team3128.common.utility.Log;
import org.team3128.common.utility.test_suite.CanDevices;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.team3128.common.generics.Threaded;
import org.team3128.common.hardware.motor.LazyCANSparkMax;

public class Shooter extends Threaded {

    public static final Shooter instance = new Shooter();
    public static LazyCANSparkMax LEFT_SHOOTER;
    public static LazyCANSparkMax RIGHT_SHOOTER;
    public static CANEncoder SHOOTER_ENCODER;

    public static boolean DEBUG = true;
    public static int setpoint = 0; // rotations per minute
    public static double output, error;

    private Shooter() {
        configMotors();
        configEncoders();
    }

    private void configMotors() {
        LEFT_SHOOTER = new LazyCANSparkMax(Constants.SHOOTER_MOTOR_LEFT_ID, MotorType.kBrushless);
        RIGHT_SHOOTER = new LazyCANSparkMax(Constants.SHOOTER_MOTOR_RIGHT_ID, MotorType.kBrushless);
        if (DEBUG) {
            Log.info("Shooter", "Config motors");
        }
    }

    private void configEncoders() {
        SHOOTER_ENCODER = LEFT_SHOOTER.getEncoder();
        if (DEBUG) {
            Log.info("Shooter", "Config encoders");
        }
    }

    public static Shooter getInstance() {
        return instance;
    }

    public static double getRPM() {
        return SHOOTER_ENCODER.getVelocity();
    }

    public static void setSetpoint(int passedSetpoint) {
        setpoint = passedSetpoint;
    }

    @Override
    public void update() {
        error = setpoint - getRPM();
        output = Constants.K_SHOOTER_P * error;
        LEFT_SHOOTER.set(output);
        RIGHT_SHOOTER.set(-output);
        if (DEBUG) {
            Log.info("Shooter", "Error  is: " + error + ", vel is: " + getRPM() + ", output is: " + output);
        }
    }
}