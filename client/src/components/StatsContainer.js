import { FaBug, FaCalendarCheck, FaSuitcaseRolling } from 'react-icons/fa'
import { useAppContext } from '../context/appContext'
import StatItem from './StatItem'
import Wrapper from '../assets/wrappers/StatsContainer'

const StatsContainer = () => {

  const { stats } = useAppContext()

  if(stats?.stats?.length === 0)
  {
    return <h1>No stats found!</h1>
  }
  
  const defaultStats = [
    {
      title: 'pending applications',
      count: stats?.stats?.map(stat => stat.status === "PENDING" ? stat.count : " ") || 0,
      icon: <FaSuitcaseRolling />,
      color: '#e9b949',
      bcg: '#fcefc7',
    },
    {
      title: 'interviews scheduled',
      count: stats?.stats?.map(stat => stat.status === "INTERVIEW" ? stat.count : " ") || 0,
      icon: <FaCalendarCheck />,
      color: '#647acb',
      bcg: '#e0e8f9',
    },
    {
      title: 'jobs declined',
      count: stats?.stats?.map(stat => stat.status === "DECLINED" ? stat.count : " ") || 0,
      icon: <FaBug />,
      color: '#d66a6a',
      bcg: '#ffeeee',
    },
  ]

  return (
    <Wrapper>
      {defaultStats?.map((item, index) => {
        return <StatItem key={index} {...item} />
      })}
    </Wrapper>
  )
}

export default StatsContainer