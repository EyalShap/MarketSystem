import React, { useEffect, useState } from 'react';
import '../styles/profile.css';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { getMember } from '../API';
import { useParams } from 'react-router-dom';
import MemberModel from '../models/MemberModel';


const schema = yup.object().shape({
  username: yup.string().required("Username is required"),
  firstName: yup.string().required("First Name is required"),
  lastName: yup.string().required("Last Name is required"),
  email: yup.string().email("Invalid email format").required("Email is required"),
  phoneNumber: yup.string().required("Phone number is required"),
  birthday: yup.string().required("Birthday is required")
});

const Profile = () => {
  const {username} = useParams();
  const [defaultValues, setDefaultValues] = useState({username: '', firstName: '', lastName: '', email: '', phoneNumber: '', birthday: ''});  

  useEffect(() => {
    const fetchProfile = async () => {
      const profileData = await getMember(username as string);
      setDefaultValues(profileData as MemberModel);
    };

    fetchProfile();
  }, []);

  const {
    register,
    handleSubmit,
    formState: { errors, touchedFields },
    reset,
    trigger
  } = useForm({
    resolver: yupResolver(schema),
    defaultValues: defaultValues
  });
  useEffect(() => {
    if (defaultValues) {
      reset(defaultValues); // Reset the form with fetched data
    }
  }, [defaultValues, reset]);

  const onSubmit = (data: any) => {
    console.log('Profile Saved', data);
  };

  const handleReset = () => {
    reset();
  };

  return (
    <div className="profile-page">
      <h1>Profile</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            {...register("username", { onChange: () => trigger("username") })}
            className={errors.username && touchedFields.username ? 'invalid' : ''}
            disabled={true}
          />
          {errors.username && <p className="error-message">{errors.username.message}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            {...register("firstName", { onChange: () => trigger("firstName") })}
            className={errors.firstName && touchedFields.firstName ? 'invalid' : ''}
          />
          {errors.firstName && <p className="error-message">{errors.firstName.message}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="lastName">Last Name:</label>
          <input
            type="text"
            id="lastName"
            {...register("lastName", { onChange: () => trigger("lastName") })}
            className={errors.lastName && touchedFields.lastName ? 'invalid' : ''}
          />
          {errors.lastName && <p className="error-message">{errors.lastName.message}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            {...register("email", { onChange: () => trigger("email") })}
            className={errors.email && touchedFields.email ? 'invalid' : ''}
          />
          {errors.email && <p className="error-message">{errors.email.message}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="phoneNumber">Phone Number:</label>
          <input
            type="tel"
            id="phoneNumber"
            {...register("phoneNumber", { onChange: () => trigger("phoneNumber") })}
            className={errors.phoneNumber && touchedFields.phoneNumber ? 'invalid' : ''}
          />
          {errors.phoneNumber && <p className="error-message">{errors.phoneNumber.message}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="birthday">Birthday:</label>
          <input
            type="date"
            id="birthday"
            {...register("birthday", { onChange: () => trigger("birthday") })}
            className={errors.birthday && touchedFields.birthday ? 'invalid' : ''}
          />
          {errors.birthday && <p className="error-message">{errors.birthday.message}</p>}
        </div>

        <div className="button-group">
          <button type="submit" className="submit-profile-page-button">Save</button>
          <button type="button" className="reset-profile-page-button" onClick={handleReset}>Discard</button>
        </div>
      </form>
    </div>
  );
};

export default Profile;
