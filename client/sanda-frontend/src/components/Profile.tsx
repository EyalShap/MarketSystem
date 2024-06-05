import React, { useState } from 'react';
import '../styles/profile.css';

const Profile = () => {
  const [profile, setProfile] = useState({
    username: 'idanasis ',
    firstName: 'Idan',
    lastName: 'Asis',
    email: 'idanasis86@gmail.com',
    phoneNumber: '0523072999',
    birthday:"2024-06-06",
  });
  const [prevProfile, setPrevProfile] = useState({
    username: 'idanasis ',
    firstName: 'Idan',
    lastName: 'Asis',
    email: 'idanasis86@gmail.com',
    phoneNumber: '0523072999',
    birthday: "2024-06-06"
  })
  
  const handleInputChange = (event:any) => {
    const { name, value } = event.target;
    setProfile((prevProfile) => ({ ...prevProfile, [name]: value }));
  };
  const handleSave = (event:any) => {
    //setProfile(prevProfile);
  }
  const handleReset = (event:any) => {
    setProfile(prevProfile);
  }

  return (
    <div className="profile-page">
      <h1>Profile</h1>
      <label htmlFor="username">Username:</label>
      <input
        type="text"
        id="username"
        name="username"
        value={profile.username}
        disabled={true}
        onChange={handleInputChange}
      />
      <label htmlFor="firstName">First Name:</label>
      <input
        type="text"
        id="firstName"
        name="firstName"
        value={profile.firstName}
        onChange={handleInputChange}
      />
      <label htmlFor="lastName">Last Name:</label>
      <input
        type="text"
        id="lastName"
        name="lastName"
        value={profile.lastName}
        onChange={handleInputChange}
      />
      <label htmlFor="email">Email:</label>
      <input
        type="email" // Set input type to email for validation
        id="email"
        name="email"
        value={profile.email}
        onChange={handleInputChange}
      />
      <label htmlFor="phoneNumber">Phone Number:</label>
      <input
        type="tel" // Set input type to tel for phone number formatting
        id="phoneNumber"
        name="phoneNumber"
        value={profile.phoneNumber}
        onChange={handleInputChange}
      />
      <label htmlFor="birthday">Birthday:</label>
      <input
        type="date" // Set input type to date for date picker
        id="birthday"
        name="birthday"
        value={profile.birthday}
        onChange={handleInputChange}
      />
      <button type="submit" className='profile-page-button' onClick={handleSave}>Save</button>
      <button type="reset" className='reset-profile-page-button' onClick={handleReset}>discard</button>
    </div>
  );
};

export default Profile;
