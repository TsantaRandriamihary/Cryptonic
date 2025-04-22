import { Redirect, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
/* import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css'; */

/**
 * Ionic Dark Mode
 * -----------------------------------------------------
 * For more info, please see:
 * https://ionicframework.com/docs/theming/dark-mode
 */

/* import '@ionic/react/css/palettes/dark.always.css'; */
/* import '@ionic/react/css/palettes/dark.class.css'; */
import '@ionic/react/css/palettes/dark.system.css';

/* Theme variables */
import './theme/variables.css';
import './theme/css/bootstrap-icons.css'
import './theme/css/bootstrap.css';
import './theme/css/bold.css';
import './theme/css/perfect-scrollbar.css';
import './theme/css/app.css';
import './theme/css/auth.css';
import './theme/css/custom.css'

/* Pages */
import Home from './pages/Home';
import Modify_profil from "./pages/Modify_profil";
import Depot from "./pages/Depot";
import Retrait from "./pages/Retrait";
import Evolution from "./pages/Evolution";

setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <Route exact path="/home">
          <Home />
        </Route>

        {/* Page navigation */}
        <Route exact path="/depot">
          <Depot />
        </Route>
        <Route exact path="/retrait">
          <Retrait />
        </Route>
        <Route exact path="/modify">
          <Modify_profil />
        </Route>
        <Route exact path="/evolution">
          <Evolution />
        </Route>

        <Route exact path="/">
          <Redirect to="/home" />
        </Route>
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;
