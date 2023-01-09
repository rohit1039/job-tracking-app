import links from '../utils/links';
import { NavLink } from 'react-router-dom';

const NavLinks = ({ toggleSideBar }) => {

    const userId = localStorage.getItem("userId")

    return (
        <div className='nav-links'>
            {links.map((link) => {

                link.path = (link.path === "profile" || link.path === "add-job") ? `${link.path}/${userId}` : link.path

                const { text, path, id, icon } = link;

                return (
                    <NavLink reloadDocument
                        to={path}
                        key={id}
                        onClick={toggleSideBar}
                        className={({ isActive }) =>
                            isActive ? 'nav-link active' : 'nav-link'
                        }
                        end
                    >
                        <span className='icon'>{icon}</span>
                        {text}
                    </NavLink>
                );
            })}
        </div>
    );
};

export default NavLinks;