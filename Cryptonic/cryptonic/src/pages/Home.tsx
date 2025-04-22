import React from "react";

const Home: React.FC = () => {
    return (
        <div id="auth">

            <div className="row h-100">
                <div className="col-lg-5 col-12">
                    <div id="auth-left">
                        <div className="auth-logo">
                            <h1 className="audiowide"> <a href="home"><img src="/img/logo_final.svg" alt="Logo"/> Cryptonic </a> </h1>
                        </div>
                        <h1 className="auth-subtitle">Connexion</h1>

                        <form action="index.html">
                            <div className="form-group position-relative has-icon-left mb-4">
                                <input type="email" className="form-control form-control-xl" placeholder="E-mail"/>
                                <div className="form-control-icon">
                                    <i className="bi bi-envelope"></i>
                                </div>
                            </div>
                            <div className="form-group position-relative has-icon-left mb-4">
                                <input type="password" className="form-control form-control-xl" placeholder="Mot de passe"/>
                                <div className="form-control-icon">
                                    <i className="bi bi-shield-lock"></i>
                                </div>
                            </div>
                            <button className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Se connecter</button>
                        </form>
                        <div className="text-center mt-5 text-lg fs-4">
                            <p className="text-gray-600">Pas encore inscrit ? <a href="modify"
                                                                                   className="font-bold">S'inscrire</a>.</p>
                        </div>
                    </div>
                </div>
                <div className="col-lg-7 d-none d-lg-block">
                    <div id="auth-right">

                    </div>
                </div>
            </div>

        </div>
    );
};

export default Home;


// import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar } from '@ionic/react';
// import ExploreContainer from '../components/ExploreContainer';
// import './Home.css';
//
// const Home: React.FC = () => {
//   return (
//     <IonPage>
//       <IonHeader>
//         <IonToolbar>
//           <IonTitle>Blank</IonTitle>
//         </IonToolbar>
//       </IonHeader>
//       <IonContent fullscreen>
//         <IonHeader collapse="condense">
//           <IonToolbar>
//             <IonTitle size="large">Blank</IonTitle>
//           </IonToolbar>
//         </IonHeader>
//         <ExploreContainer />
//       </IonContent>
//     </IonPage>
//   );
// };
//
// export default Home;
