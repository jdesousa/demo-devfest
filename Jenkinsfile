@Library("lm.turbine")
import lm.turbine.*

properties([
    parameters([
        booleanParam( name: 'enable_it_tests', defaultValue: true, description: 'IT tests (maven profile AllTests)?'),
        booleanParam(name: 'release', description: 'Perform a release?', defaultValue: false),
        /* its required to add a default value for credential otherwise gitlab hooks are broken */
        credentials(name: 'ldap', credentialType: "Username with password", defaultValue: 'unexisting_credential', description: 'Required if release mode enabled. You need to create your own credential first to use this build: top right user menu > Credentials (left menu) > User (sub menu) > Global identifiers (central table) > Add identifier (left menu) - Name it as you wish and enter your LDAP login and password (each password update must be applied here too).' )
    ])
])

/*
To understand this script or for a customized turbine experience:
http://gitlab-xnet.fr.corp.leroymerlin.com/fr-lm-turbine/turbine-jenkins-libs.git
*/

new Turbine(this).models.java.snapshot \
    app: 'data-integration-product-core',
    group: '10114660'
