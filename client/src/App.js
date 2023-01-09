import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Error, Landing, Register } from './pages';
import ProtectedRoute from './pages/ProtectedRoute';
import { AddJob, AllJobs, AllUsers, Profile, SharedLayout, Stats } from './pages/dashboard'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<ProtectedRoute><SharedLayout /></ProtectedRoute>}>
          <Route index element={<Stats />} />
          <Route path='all-users' element={<AllUsers />} />
          <Route path='add-job/:userId' element={<AddJob />} />
          <Route path='all-jobs' element={<AllJobs />} />
          <Route path="/profile/:userId" element={<Profile />} />
        </Route>
        <Route path='/register' element={<Register />} />
        <Route path='/landing' element={<Landing />} />
        <Route path='*' element={<Error />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
