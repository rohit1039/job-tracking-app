import React from 'react'
import { useState, useEffect } from 'react';
import { FormRow, Logo, Alert } from '../components';
import RegisterPage from '../assets/wrappers/RegisterPage';
import { AnimateForm } from '../components/AnimateForm';
import { useAppContext } from '../context/appContext';
import { useNavigate } from 'react-router-dom';

const Register = () => {

    const navigate = useNavigate();

    const initialState = {
        firstName: '',
        lastName: '',
        emailID: '',
        password: '',
        location: '',
        token: '',
        fullName: '',
        age: '',
        isMember: true
    }

    const [values, setValues] = useState(initialState)

    const { token, isLoading, showAlert, displayAlert, setupUser } = useAppContext()

    const handleChange = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value })
    }

    const onSubmit = (e) => {
        e.preventDefault();
        const { firstName, lastName, emailID, password, location, age, isMember } = values;
        if (!emailID || !password || (!isMember && (!firstName || !lastName || !location || !age))) {
            displayAlert();
            return;
        }
        const currentUser = { firstName, lastName, emailID, password, location, age }
        if (isMember) {
            setupUser({
                currentUser,
                endPoint: 'login',
                alertText: 'Login Successful! Redirecting...',
            });
        }
        else {
            setupUser({
                currentUser,
                endPoint: 'register',
                alertText: 'User Registered! Redirecting...',
            });
            setValues({
                firstName: "",
                lastName: "",
                emailID: "",
                password: "",
                location: "",
                age: ""
            })
        }
    };

    useEffect(() => {
        if (token) {
            setTimeout(() => {
                navigate('/');
            }, 3000);
        }
    }, [token, navigate]);

    const toggleMember = () => {
        setValues({ ...values, isMember: !values.isMember })
    }

    return (
        <RegisterPage className='full-page'>
            <AnimateForm>
                {!values.isMember ? (<form className='form-register' onSubmit={onSubmit}>
                    <Logo />
                    <h3>Register</h3>
                    {showAlert && <Alert />}
                    <>
                        <div className='form-row-style' >
                            <FormRow type="text" name="firstName" placeholder="Enter your first-name" value={values.firstName} handleChange={handleChange} />
                            <FormRow type="text" name="lastName" placeholder="Enter your last-name" value={values.lastName} handleChange={handleChange} /></div>
                        <div className='form-row-style' >
                            <FormRow type="text" name="location" placeholder="Enter your location" value={values.location} handleChange={handleChange} />
                            <FormRow type="number" name="age" placeholder="Enter your age" value={values.age} handleChange={handleChange} /> </div>
                        <div>
                            <FormRow type="email" name="emailID" placeholder="Enter your emailID" value={values.emailID} handleChange={handleChange} />
                            <FormRow type="password" name="password" placeholder="Enter your password" value={values.password} handleChange={handleChange} />
                        </div>
                        <button type="submit" onSubmit={onSubmit} className='btn btn-block-register' disabled={isLoading}>submit</button>
                        <button
                            type='button'
                            className='btn btn-block-register btn-hipster-register'
                            disabled={isLoading}
                            onClick={() => {
                                setupUser({
                                    currentUser: { emailID: 'testuser@yahoo.com', password: 'Test@7978' },
                                    endPoint: 'login',
                                    alertText: 'Login Successful! Redirecting...',
                                });
                            }}
                        >{isLoading ? 'loading...' : 'demo app'}</button>
                        <p>
                            {values.isMember ? "Not a member yet?" : "Already have an account?"}
                            <button type="button" className='member-btn' onClick={toggleMember}>{values.isMember ? 'Register' : 'Login'} </button>
                        </p>
                    </>
                </form>
                ) : (<form className='form' onSubmit={onSubmit}> <Logo />
                    <h3>Login</h3>
                    {showAlert && <Alert />}
                    <FormRow type="email" name="emailID" placeholder="Enter your username" value={values.emailID} handleChange={handleChange} />
                    <FormRow type="password" name="password" placeholder="Enter your password" value={values.password} handleChange={handleChange} />
                    <button type="submit" onSubmit={onSubmit} className='btn btn-block' disabled={isLoading}>submit</button>
                    <button
                        type='button'
                        className='btn btn-block btn-hipster'
                        disabled={isLoading}
                        onClick={() => {
                            setupUser({
                                currentUser: { emailID: 'testuser@yahoo.com', password: 'Test@7978' },
                                endPoint: 'login',
                                alertText: 'Login Successful! Redirecting...',
                            });
                        }}
                    >{isLoading ? 'loading...' : 'demo app'}</button>
                    <p>
                        {values.isMember ? "Not a member yet?" : "Already have an account?"}
                        <button type="button" className='member-btn' onClick={toggleMember}>{values.isMember ? 'Register' : 'Login'} </button>
                    </p>
                </form>)}
            </AnimateForm>
        </RegisterPage >
    )
}

export default Register;